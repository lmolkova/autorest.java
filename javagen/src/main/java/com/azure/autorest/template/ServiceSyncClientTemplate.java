// Copyright (c) Microsoft Corporation. All rights reserved.
// Licensed under the MIT License.

package com.azure.autorest.template;

import com.azure.autorest.extension.base.plugin.JavaSettings;
import com.azure.autorest.model.clientmodel.AsyncSyncClient;
import com.azure.autorest.model.clientmodel.ClientMethod;
import com.azure.autorest.model.clientmodel.ConvenienceMethod;
import com.azure.autorest.model.clientmodel.GenericType;
import com.azure.autorest.model.clientmodel.MethodGroupClient;
import com.azure.autorest.model.clientmodel.ServiceClient;
import com.azure.autorest.model.javamodel.JavaClass;
import com.azure.autorest.model.javamodel.JavaContext;
import com.azure.autorest.model.javamodel.JavaFile;
import com.azure.autorest.model.javamodel.JavaVisibility;
import com.azure.autorest.util.ClientModelUtil;
import com.azure.autorest.util.TemplateUtil;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Template to create a synchronous client.
 */
public class ServiceSyncClientTemplate implements IJavaTemplate<AsyncSyncClient, JavaFile>  {

  private static final ServiceSyncClientTemplate INSTANCE = new ServiceSyncClientTemplate();

  protected ServiceSyncClientTemplate() {
  }

  public static ServiceSyncClientTemplate getInstance() {
    return INSTANCE;
  }

  @Override
  public final void write(AsyncSyncClient syncClient, JavaFile javaFile) {
    final ServiceClient serviceClient = syncClient.getServiceClient();

    JavaSettings settings = JavaSettings.getInstance();
    final String syncClassName = syncClient.getClassName();
    final MethodGroupClient methodGroupClient = syncClient.getMethodGroupClient();
    final boolean wrapServiceClient = methodGroupClient == null;
    final String builderPackageName = ClientModelUtil.getServiceClientBuilderPackageName(serviceClient);
    final String builderClassName = serviceClient.getInterfaceName() + ClientModelUtil.getBuilderSuffix();
    final boolean samePackageAsBuilder = builderPackageName.equals(syncClient.getPackageName());
    final JavaVisibility constructorVisibility = samePackageAsBuilder ? JavaVisibility.PackagePrivate : JavaVisibility.Public;

    Set<String> imports = new HashSet<>();
    if (wrapServiceClient) {
      serviceClient.addImportsTo(imports, true, false, settings);
      imports.add(serviceClient.getPackage() + "." + serviceClient.getClassName());
    } else {
      methodGroupClient.addImportsTo(imports, true, settings);
      imports.add(methodGroupClient.getPackage() + "." + methodGroupClient.getClassName());
    }
    imports.add(builderPackageName + "." + builderClassName);
    addServiceClientAnnotationImport(imports);

    Templates.getConvenienceSyncMethodTemplate().addImports(imports, syncClient.getConvenienceMethods());

    javaFile.declareImport(imports);
    javaFile.javadocComment(comment ->
        comment.description(String.format("Initializes a new instance of the synchronous %1$s type.",
            serviceClient.getInterfaceName())));

    if (syncClient.getClientBuilder() != null) {
      javaFile.annotation(String.format("ServiceClient(builder = %s.class)", syncClient.getClientBuilder().getClassName()));
    }
    javaFile.publicFinalClass(syncClassName, classBlock -> {
      writeClass(syncClient, classBlock, constructorVisibility);
    });
  }

  /**
   * Extension to write the sync client class.
   *
   * @param syncClient the sync client
   * @param classBlock the class block to write
   * @param constructorVisibility the visibility of class constructor
   */
  protected void writeClass(AsyncSyncClient syncClient, JavaClass classBlock, JavaVisibility constructorVisibility) {
    final ServiceClient serviceClient = syncClient.getServiceClient();
    final MethodGroupClient methodGroupClient = syncClient.getMethodGroupClient();
    final boolean wrapServiceClient = methodGroupClient == null;

    // Add service client member
    addGeneratedAnnotation(classBlock);
    if (wrapServiceClient) {
      classBlock.privateFinalMemberVariable(serviceClient.getClassName(), "serviceClient");
    } else {
      classBlock.privateFinalMemberVariable(methodGroupClient.getClassName(), "serviceClient");
    }

    // Service Client Constructor
    classBlock.javadocComment(comment -> {
      comment.description(String.format("Initializes an instance of %1$s class.", syncClient.getClassName()));
      comment.param("serviceClient", "the service client implementation.");
    });
    addGeneratedAnnotation(classBlock);
    if (wrapServiceClient) {
      classBlock.constructor(constructorVisibility, String.format("%1$s(%2$s %3$s)", syncClient.getClassName(),
          serviceClient.getClassName(), "serviceClient"), constructorBlock -> {
        constructorBlock.line("this.serviceClient = serviceClient;");
      });
    } else {
      classBlock.constructor(constructorVisibility, String.format("%1$s(%2$s %3$s)", syncClient.getClassName(),
          methodGroupClient.getClassName(), "serviceClient"), constructorBlock -> {
        constructorBlock.line("this.serviceClient = serviceClient;");
      });
    }

    writeMethods(syncClient, classBlock);
  }

  /**
   * Extension to write the sync client methods.
   *
   * @param syncClient the sync client
   * @param classBlock the class block to write
   */
  protected void writeMethods(AsyncSyncClient syncClient, JavaClass classBlock) {
    final ServiceClient serviceClient = syncClient.getServiceClient();
    final MethodGroupClient methodGroupClient = syncClient.getMethodGroupClient();

    final boolean useMethodGroupClient = methodGroupClient != null;
    List<ClientMethod> clientMethods = serviceClient.getClientMethods();
    if(useMethodGroupClient) {
      clientMethods = methodGroupClient.getClientMethods();
    }

    clientMethods.stream()
        .filter(clientMethod -> clientMethod.getMethodVisibility() == JavaVisibility.Public)
        .filter(clientMethod -> !clientMethod.isImplementationOnly())
        .filter(clientMethod -> !clientMethod.getType().name().contains("Async"))
        .forEach(clientMethod -> {
          writeMethod(clientMethod, classBlock);
        });

    writeConvenienceMethods(syncClient.getConvenienceMethods(), classBlock);

    ServiceAsyncClientTemplate.addEndpointMethod(classBlock, syncClient.getClientBuilder(), serviceClient, this.clientReference());
  }

  /**
   * Extension for client reference. Usually be either "this.serviceClient" or "this.client".
   *
   * @return the code for client reference.
   */
  protected String clientReference() {
    return "this.serviceClient";
  }

  /**
   * Extension to write the sync client method.
   *
   * @param clientMethod the client method in implementation class
   * @param classBlock the class block to write
   */
  protected void writeMethod(ClientMethod clientMethod, JavaClass classBlock) {
    Templates.getWrapperClientMethodTemplate().write(clientMethod, classBlock);
  }

  protected void addServiceClientAnnotationImport(Set<String> imports) {
    imports.add("com.azure.core.annotation.ServiceClient");
    imports.add("com.azure.core.annotation.Generated");
  }

  protected void addGeneratedAnnotation(JavaContext classBlock) {
    classBlock.annotation("Generated");
  }

  private static void writeConvenienceMethods(List<ConvenienceMethod> convenienceMethods, JavaClass classBlock) {
    Set<GenericType> typeReferenceStaticClasses = new HashSet<>();

    convenienceMethods.forEach(m -> Templates.getConvenienceSyncMethodTemplate().write(m, classBlock, typeReferenceStaticClasses));

    // static variables for TypeReference<T>
    for (GenericType typeReferenceStaticClass : typeReferenceStaticClasses) {
      TemplateUtil.writeTypeReferenceStaticVariable(classBlock, typeReferenceStaticClass);
    }
  }
}
