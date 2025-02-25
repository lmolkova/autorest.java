// Copyright (c) Microsoft Corporation. All rights reserved.
// Licensed under the MIT License.

package com.azure.autorest.template;

import com.azure.autorest.extension.base.model.codemodel.RequestParameterLocation;
import com.azure.autorest.extension.base.plugin.JavaSettings;
import com.azure.autorest.model.clientmodel.AsyncSyncClient;
import com.azure.autorest.model.clientmodel.ClassType;
import com.azure.autorest.model.clientmodel.ClientBuilder;
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
import com.azure.autorest.util.ModelNamer;
import com.azure.autorest.util.TemplateUtil;
import com.azure.core.client.traits.EndpointTrait;
import com.azure.core.util.CoreUtils;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Template to create an asynchronous client.
 */
public class ServiceAsyncClientTemplate implements IJavaTemplate<AsyncSyncClient, JavaFile> {

  private static final ServiceAsyncClientTemplate INSTANCE = new ServiceAsyncClientTemplate();

  protected ServiceAsyncClientTemplate() {
  }

  public static ServiceAsyncClientTemplate getInstance() {
    return INSTANCE;
  }

  @Override
  public final void write(AsyncSyncClient asyncClient, JavaFile javaFile) {
    ServiceClient serviceClient = asyncClient.getServiceClient();

    JavaSettings settings = JavaSettings.getInstance();
    String asyncClassName = asyncClient.getClassName();
    MethodGroupClient methodGroupClient = asyncClient.getMethodGroupClient();
    final boolean wrapServiceClient = methodGroupClient == null;
    final String builderPackageName = ClientModelUtil.getServiceClientBuilderPackageName(serviceClient);
    final String builderClassName = serviceClient.getInterfaceName() + ClientModelUtil.getBuilderSuffix();
    final boolean samePackageAsBuilder = builderPackageName.equals(asyncClient.getPackageName());
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
    addServiceClientAnnotationImports(imports);

    Templates.getConvenienceAsyncMethodTemplate().addImports(imports, asyncClient.getConvenienceMethods());

    javaFile.declareImport(imports);
    javaFile.javadocComment(comment ->
        comment.description(String.format("Initializes a new instance of the asynchronous %1$s type.",
            serviceClient.getInterfaceName())));

    if (asyncClient.getClientBuilder() != null) {
      javaFile.annotation(String.format("ServiceClient(builder = %s.class, isAsync = true)", asyncClient.getClientBuilder().getClassName()));
    }
    javaFile.publicFinalClass(asyncClassName, classBlock ->
    {
      // Add service client member variable
      addGeneratedAnnotation(classBlock);
      if (wrapServiceClient) {
        classBlock.privateFinalMemberVariable(serviceClient.getClassName(), "serviceClient");
      } else {
        classBlock.privateFinalMemberVariable(methodGroupClient.getClassName(), "serviceClient");
      }

      // Service Client Constructor
      classBlock.javadocComment(comment -> {
        comment.description(String.format("Initializes an instance of %1$s class.", asyncClient.getClassName()));
        comment.param("serviceClient", "the service client implementation.");
      });
      addGeneratedAnnotation(classBlock);
      if (wrapServiceClient) {
        classBlock.constructor(constructorVisibility, String.format("%1$s(%2$s %3$s)", asyncClassName,
            serviceClient.getClassName(), "serviceClient"), constructorBlock -> {
          constructorBlock.line("this.serviceClient = serviceClient;");
        });
      } else {
        classBlock.constructor(constructorVisibility, String.format("%1$s(%2$s %3$s)", asyncClassName,
            methodGroupClient.getClassName(), "serviceClient"), constructorBlock -> {
          constructorBlock.line("this.serviceClient = serviceClient;");
        });
      }

      if (wrapServiceClient) {
        serviceClient.getClientMethods().stream()
            .filter(clientMethod -> clientMethod.getMethodVisibility() == JavaVisibility.Public)
            .filter(clientMethod -> !clientMethod.isImplementationOnly())
            .filter(clientMethod -> clientMethod.getType().name().contains("Async"))
            .filter(clientMethod -> !clientMethod.getMethodParameters()
                .stream()
                .anyMatch(methodParam -> methodParam.getWireType().contains(ClassType.Context)))
            .forEach(clientMethod -> {
              Templates.getWrapperClientMethodTemplate().write(clientMethod, classBlock);
            });
      } else {
        methodGroupClient.getClientMethods().stream()
            .filter(clientMethod -> clientMethod.getMethodVisibility() == JavaVisibility.Public)
            .filter(clientMethod -> !clientMethod.isImplementationOnly())
            .filter(clientMethod -> clientMethod.getType().name().contains("Async"))
            .filter(clientMethod -> !clientMethod.getMethodParameters()
                .stream()
                .anyMatch(methodParam -> methodParam.getWireType().contains(ClassType.Context)))
            .forEach(clientMethod -> {
              Templates.getWrapperClientMethodTemplate().write(clientMethod, classBlock);
            });
      }

      writeConvenienceMethods(asyncClient.getConvenienceMethods(), classBlock);

      ServiceAsyncClientTemplate.addEndpointMethod(classBlock, asyncClient.getClientBuilder(), serviceClient, "this.serviceClient");
    });
  }

  protected void addServiceClientAnnotationImports(Set<String> imports) {
    imports.add("com.azure.core.annotation.ServiceClient");
    imports.add("com.azure.core.annotation.Generated");
  }

  protected void addGeneratedAnnotation(JavaContext classBlock) {
    classBlock.annotation("Generated");
  }

  /**
   * Adds "getEndpoint" method, if necessary.
   * <p>
   * This method is companion to "sendRequest" method. Without endpoint, the URL in sendRequest is hard to compose.
   *
   * @param classBlock the class block for writing the method.
   * @param clientBuilder the client builder.
   * @param clientReference the code for client reference. E.g. "this.serviceClient" or "this.client".
   */
  static void addEndpointMethod(JavaClass classBlock, ClientBuilder clientBuilder, ServiceClient serviceClient, String clientReference) {
    // expose "getEndpoint" as public, as companion to "sendRequest" method
    if (JavaSettings.getInstance().isGenerateSendRequestMethod()) {
      ClientMethod referenceClientMethod = !CoreUtils.isNullOrEmpty(serviceClient.getClientMethods())
          ? serviceClient.getClientMethods().iterator().next()
          : serviceClient.getMethodGroupClients().stream().flatMap(mg -> mg.getClientMethods().stream()).findFirst().orElse(null);

      if (referenceClientMethod != null) {
        final String baseUrl = serviceClient.getBaseUrl();
        final String endpointReplacementExpr = referenceClientMethod.getProxyMethod().getParameters().stream()
            .filter(p -> p.isFromClient() && p.getRequestParameterLocation() == RequestParameterLocation.URI)
            .filter(p -> baseUrl.contains(String.format("{%s}", p.getRequestParameterName())))
            .map(p -> String.format(".replace(%1$s, %2$s)",
                ClassType.String.defaultValueExpression(String.format("{%s}", p.getRequestParameterName())),
                p.getParameterReference()
            )).collect(Collectors.joining());
        final String endpointExpr = ClassType.String.defaultValueExpression(baseUrl) + endpointReplacementExpr;

        clientBuilder.getBuilderTraits().stream()
            .filter(t -> EndpointTrait.class.getSimpleName().equals(t.getTraitInterfaceName()))
            .map(t -> t.getTraitMethods().iterator().next().getProperty())
            .findAny().ifPresent(serviceClientProperty -> {
              classBlock.javadocComment(comment -> {
                comment.description("Gets the service endpoint that the client is connected to.");
                comment.methodReturns("the service endpoint that the client is connected to.");
              });
              String methodName = new ModelNamer().modelPropertyGetterName(serviceClientProperty);
              classBlock.method(serviceClientProperty.getMethodVisibility(), null, String.format("%1$s %2$s()",
                  serviceClientProperty.getType(), methodName), function -> {
                function.methodReturn(endpointExpr);
              });
            });
      }
    }
  }

  private static void writeConvenienceMethods(List<ConvenienceMethod> convenienceMethods, JavaClass classBlock) {
    Set<GenericType> typeReferenceStaticClasses = new HashSet<>();

    convenienceMethods.forEach(m -> Templates.getConvenienceAsyncMethodTemplate().write(m, classBlock, typeReferenceStaticClasses));

    // static variables for TypeReference<T>
    for (GenericType typeReferenceStaticClass : typeReferenceStaticClasses) {
      TemplateUtil.writeTypeReferenceStaticVariable(classBlock, typeReferenceStaticClass);
    }
  }
}
