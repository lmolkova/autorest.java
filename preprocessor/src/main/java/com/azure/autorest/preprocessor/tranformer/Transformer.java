// Copyright (c) Microsoft Corporation. All rights reserved.
// Licensed under the MIT License.

package com.azure.autorest.preprocessor.tranformer;

import com.azure.autorest.extension.base.model.codemodel.AndSchema;
import com.azure.autorest.extension.base.model.codemodel.BinarySchema;
import com.azure.autorest.extension.base.model.codemodel.ChoiceSchema;
import com.azure.autorest.extension.base.model.codemodel.Client;
import com.azure.autorest.extension.base.model.codemodel.CodeModel;
import com.azure.autorest.extension.base.model.codemodel.DictionarySchema;
import com.azure.autorest.extension.base.model.codemodel.Language;
import com.azure.autorest.extension.base.model.codemodel.Languages;
import com.azure.autorest.extension.base.model.codemodel.Metadata;
import com.azure.autorest.extension.base.model.codemodel.NumberSchema;
import com.azure.autorest.extension.base.model.codemodel.ObjectSchema;
import com.azure.autorest.extension.base.model.codemodel.Operation;
import com.azure.autorest.extension.base.model.codemodel.OperationGroup;
import com.azure.autorest.extension.base.model.codemodel.OrSchema;
import com.azure.autorest.extension.base.model.codemodel.Parameter;
import com.azure.autorest.extension.base.model.codemodel.Property;
import com.azure.autorest.extension.base.model.codemodel.Protocol;
import com.azure.autorest.extension.base.model.codemodel.Protocols;
import com.azure.autorest.extension.base.model.codemodel.Request;
import com.azure.autorest.extension.base.model.codemodel.RequestParameterLocation;
import com.azure.autorest.extension.base.model.codemodel.Schema;
import com.azure.autorest.extension.base.model.codemodel.Schemas;
import com.azure.autorest.extension.base.model.codemodel.SealedChoiceSchema;
import com.azure.autorest.extension.base.model.codemodel.StringSchema;
import com.azure.autorest.extension.base.model.extensionmodel.XmsExtensions;
import com.azure.autorest.extension.base.model.extensionmodel.XmsPageable;
import com.azure.autorest.extension.base.plugin.JavaSettings;
import com.azure.autorest.preprocessor.namer.CodeNamer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Transformer {

  public CodeModel transform(CodeModel codeModel) {
    renameCodeModel(codeModel);
    transformSchemas(codeModel.getSchemas());
    if (JavaSettings.getInstance().getClientFlattenAnnotationTarget() == JavaSettings.ClientFlattenAnnotationTarget.NONE) {
      markFlattenedSchemas(codeModel);
    }
    transformOperationGroups(codeModel.getOperationGroups(), codeModel);
    // multi-clients for Cadl
    if (codeModel.getClients() != null) {
      transformClients(codeModel.getClients(), codeModel);
    }
    return codeModel;
  }

  private void transformSchemas(Schemas schemas) {
    schemas.getObjects().addAll(schemas.getGroups());
    schemas.setGroups(new ArrayList<>());
    for (ObjectSchema objectSchema : schemas.getObjects()) {
      renameType(objectSchema);
      for (Property property : objectSchema.getProperties()) {
        renameProperty(property);
      }
    }
    for (AndSchema andSchema : schemas.getAnds()) {
      renameType(andSchema);
    }
    for (ChoiceSchema choiceSchema : schemas.getChoices()) {
      renameType(choiceSchema);
    }
    for (SealedChoiceSchema sealedChoiceSchema : schemas.getSealedChoices()) {
      renameType(sealedChoiceSchema);
    }
    for (DictionarySchema dictionarySchema : schemas.getDictionaries()) {
      renameType(dictionarySchema);
    }
    for (OrSchema unionSchema : schemas.getOrs()) {
      renameType(unionSchema);

      // these ObjectSchema is not added to codeModel.schemas
      for (ObjectSchema objectSchema : unionSchema.getAnyOf()) {
        renameType(objectSchema);
        for (Property property : objectSchema.getProperties()) {
          renameProperty(property);
        }
      }
    }
  }

  private void transformClients(List<Client> clients, CodeModel codeModel) {
    for (Client client : clients) {
      Language language = client.getLanguage().getDefault();
      Language java = addJavaLanguage(client);
      java.setName(CodeNamer.toPascalCase(language.getName())); // Name of client should always ends with Client, hence it should not require escaping
      java.setSerializedName(language.getSerializedName());
      java.setDescription(language.getDescription());
      client.getLanguage().setJava(java);

      if (client.getOperationGroups() != null) {
        for (OperationGroup operationGroup : client.getOperationGroups()) {
          List<Operation> pagingOperations = new ArrayList<>();

          operationGroup.setCodeModel(client);
          renameMethodGroup(operationGroup);
          for (Operation operation : operationGroup.getOperations()) {
            operation.setOperationGroup(operationGroup);

            if (operation.getExtensions() != null && operation.getExtensions().getXmsPageable() != null) {
              pagingOperations.add(operation);
            }
          }

          // paging
          for (Operation operation : pagingOperations) {
            if (nonNullNextLink(operation)) {
              addPagingNextOperation(client, operation.getOperationGroup(), operation);
            }
          }
        }
      }
    }
  }

  private void transformOperationGroups(List<OperationGroup> operationGroups, CodeModel codeModel) {
    List<Operation> pagingOperations = new ArrayList<>();
    for (OperationGroup operationGroup : operationGroups) {
      operationGroup.setCodeModel(codeModel);
      renameMethodGroup(operationGroup);
      for (Operation operation : operationGroup.getOperations()) {
        operation.setOperationGroup(operationGroup);
        renameMethod(operation);
        if (operation.getConvenienceApi() != null) {
          renameMethod(operation.getConvenienceApi());
          if (operation.getConvenienceApi().getRequests() != null) {
            for (Request request : operation.getConvenienceApi().getRequests()) {
              for (Parameter parameter : request.getParameters()) {
                renameVariable(parameter);
              }
            }
          }
        }
        for (Request request : operation.getRequests()) {
          Stream<Parameter> newParameters = Stream.concat(operation.getParameters().stream(), request.getParameters().stream());
          request.setParameters(newParameters.collect(Collectors.toList()));
          Stream<Parameter> newSignatureParameters = Stream.concat(operation.getSignatureParameters().stream(), request.getSignatureParameters().stream());
          newSignatureParameters =
                  newSignatureParameters.filter(param -> param.getGroupedBy() == null);
          request.setSignatureParameters(newSignatureParameters.collect(Collectors.toList()));
          for (int i = 0; i < request.getParameters().size(); i++) {
            Parameter parameter = request.getParameters().get(i);
            parameter.setOperation(operation);
            renameVariable(parameter);
            // add Content-Length for Flux<ByteBuffer> if not already present
            JavaSettings settings = JavaSettings.getInstance();
            if (!settings.isDataPlaneClient()) {
              if (parameter.getSchema() instanceof BinarySchema) {
                if (request.getParameters().stream().noneMatch(p -> p.getProtocol() != null
                        && p.getProtocol().getHttp() != null
                        && p.getProtocol().getHttp().getIn() == RequestParameterLocation.HEADER
                        && "content-length".equalsIgnoreCase(p.getLanguage().getDefault().getSerializedName()))) {
                  Parameter contentLength = createContentLengthParameter(operation, parameter);
                  // put contentLength parameter before input body
                  request.getParameters().add(++i, contentLength);
                  request.getSignatureParameters().add(request.getSignatureParameters().indexOf(parameter) + 1, contentLength);
                }
              }
            }
            // convert contentType to header param
            Optional<Parameter> contentType = request.getParameters().stream()
                    .filter(p -> (p.getProtocol() == null || p.getProtocol().getHttp() == null) && "contentType".equals(p.getLanguage().getDefault().getName()))
                    .findFirst();
            if (contentType.isPresent()) {
              Protocols protocols = new Protocols();
              protocols.setHttp(new Protocol());
              protocols.getHttp().setIn(RequestParameterLocation.HEADER);
              contentType.get().setProtocol(protocols);
              contentType.get().getLanguage().getDefault().setSerializedName("Content-Type");
            }
          }
          renameOdataParameterNames(request);
          deduplicateParameterNames(request);
        }

        if (operation.getExtensions() != null && operation.getExtensions().getXmsPageable() != null) {
          pagingOperations.add(operation);
        }
      }
    }

    // paging
    for (Operation operation : pagingOperations) {
      if (nonNullNextLink(operation)) {
        addPagingNextOperation(codeModel, operation.getOperationGroup(), operation);
      }
    }
  }

  private static void markFlattenedSchemas(CodeModel codeModel) {
    for (ObjectSchema objectSchema : codeModel.getSchemas().getObjects()) {
      Map<String, ObjectSchema> flattenedSchemas = null;
      for (Property property : objectSchema.getProperties()) {
        if (property.getExtensions() != null && property.getExtensions().isXmsClientFlatten() && property.getSchema() instanceof ObjectSchema) {
          ObjectSchema flattenedSchema = (ObjectSchema) property.getSchema();

          boolean isPolymorphic = flattenedSchema.getDiscriminator() != null || flattenedSchema.getDiscriminatorValue() != null;
          if (isPolymorphic) {
//            LOGGER.warn("x-ms-client-flatten is not allowed on polymorphic model '{}', on property '{}'", flattenedSchema.getLanguage().getJava().getName(), property.getLanguage().getJava().getName());
            property.getExtensions().setXmsClientFlatten(false);
            continue;
          }

          if (flattenedSchemas == null) {
            flattenedSchemas = new HashMap<>();
          }
          flattenedSchemas.put(property.getLanguage().getJava().getName(), flattenedSchema);

          // mark as flattened schema
          flattenedSchema.setFlattenedSchema(true);
        }
      }
    }
  }

  private static boolean nonNullNextLink(Operation operation) {
    return operation.getExtensions().getXmsPageable().getNextLinkName() != null && !operation.getExtensions().getXmsPageable().getNextLinkName().isEmpty();
  }

  private static class PagingNextOperationSignature {
    private final String operationGroup;
    private final String operationName;

    private PagingNextOperationSignature(String operationGroup, String operationName) {
      this.operationGroup = operationGroup;
      this.operationName = operationName;
    }

    @Override
    public boolean equals(Object o) {
      if (this == o) return true;
      if (o == null || getClass() != o.getClass()) return false;
      PagingNextOperationSignature that = (PagingNextOperationSignature) o;
      return Objects.equals(operationGroup, that.operationGroup) && Objects.equals(operationName, that.operationName);
    }

    @Override
    public int hashCode() {
      return Objects.hash(operationGroup, operationName);
    }
  }

  private final Map<PagingNextOperationSignature, Schema> pagingNextOperationResponseSchemaMap = new HashMap<>();

  private void addPagingNextOperation(Client client, OperationGroup operationGroup, Operation operation) {
    String operationGroupName;
    String operationName;
    if (operation.getExtensions().getXmsPageable().getOperationName() != null) {
      String operationGroupAndName = operation.getExtensions().getXmsPageable().getOperationName();
      String operationNameTmp;
      if (operationGroupAndName.contains("_")) {
        String[] parts = operationGroupAndName.split("_", 2);
        operationGroupName = CodeNamer.getMethodGroupName(parts[0]);
        operationNameTmp = CodeNamer.getMethodName(parts[1]);
      } else {
        operationGroupName = operationGroup.getLanguage().getJava().getName();
        operationNameTmp = CodeNamer.getMethodName(operationGroupAndName);
      }

      if (!operation.getResponses().isEmpty() && operation.getResponses().iterator().next().getSchema() != null) {
        Schema responseSchema = operation.getResponses().iterator().next().getSchema();
        PagingNextOperationSignature signature = new PagingNextOperationSignature(operationGroupName, operationNameTmp);
        if (pagingNextOperationResponseSchemaMap.containsKey(signature) && pagingNextOperationResponseSchemaMap.get(signature) != responseSchema) {
          // method signature conflict for different response schema, try a different operation name
          operationName = operation.getLanguage().getJava().getName() + "Next";
          signature = new PagingNextOperationSignature(operationGroupName, operationName);
        } else {
          operationName = operationNameTmp;
        }
        pagingNextOperationResponseSchemaMap.put(signature, responseSchema);
      } else {
        operationName= operationNameTmp;
      }
    } else {
      operationGroupName = operationGroup.getLanguage().getJava().getName();
      operationName = operation.getLanguage().getJava().getName() + "Next";
    }
    if (!client.getOperationGroups().stream()
        .anyMatch(og -> og.getLanguage().getJava().getName().equals(operationGroupName))) {
      OperationGroup newOg = new OperationGroup();
      newOg.setCodeModel(client);
      newOg.set$key(operationGroupName);
      newOg.setOperations(new ArrayList<>());
      newOg.setExtensions(operationGroup.getExtensions());
      newOg.setLanguage(new Languages());
      newOg.getLanguage().setJava(new Language());
      newOg.getLanguage().getJava().setName(operationGroupName);
      newOg.getLanguage().getJava().setDescription(operationGroup.getLanguage().getJava().getDescription());
      newOg.setProtocol(operationGroup.getProtocol());

      client.getOperationGroups().add(newOg);
      operationGroup = newOg;
    }

    if (!operationGroup.getOperations().stream()
        .anyMatch(o -> o.getLanguage().getJava().getName().equals(operationName))) {
      Operation nextOperation = new Operation();
      nextOperation.setOperationGroup(operationGroup);
      nextOperation.set$key(operationName);
      nextOperation.setLanguage(new Languages());
      nextOperation.getLanguage().setJava(new Language());
      nextOperation.getLanguage().getJava().setName(operationName);
      nextOperation.getLanguage().getJava().setDescription("Get the next page of items");
      nextOperation.setRequests(new ArrayList<>());
      Request request = new Request();
      nextOperation.getRequests().add(request);
      nextOperation.getRequests().get(0).setProtocol(new Protocols());
      nextOperation.getRequests().get(0).getProtocol().setHttp(new Protocol());
      nextOperation.getRequests().get(0).getProtocol().getHttp().setPath("{nextLink}");
      nextOperation.getRequests().get(0).getProtocol().getHttp()
          .setUri(operation.getRequests().get(0).getProtocol().getHttp().getUri());
      nextOperation.getRequests().get(0).getProtocol().getHttp().setMethod("get");
      nextOperation.getRequests().get(0).setExtensions(operation.getRequests().get(0).getExtensions());
      nextOperation.getRequests().get(0).setLanguage(operation.getLanguage());
      Parameter nextLink = new Parameter();
      nextLink.setOperation(nextOperation);
      nextLink.setImplementation(Parameter.ImplementationLocation.METHOD);
      nextLink.set$key("nextLink");
      nextLink.setNullable(false);
      nextLink.setSummary("The URL to get the next list of items");
      nextLink.setSchema(new StringSchema());
      nextLink.setRequired(true);
      nextLink.setLanguage(new Languages());
      nextLink.getLanguage().setJava(new Language());
      nextLink.getLanguage().getJava().setName("nextLink");
      nextLink.getLanguage().getJava().setSerializedName("nextLink");
      nextLink.getLanguage().setDefault(nextLink.getLanguage().getJava());
      nextLink.setProtocol(new Protocols());
      nextLink.getProtocol().setHttp(new Protocol());
      nextLink.getProtocol().getHttp().setIn(RequestParameterLocation.PATH);
      nextLink.setExtensions(new XmsExtensions());
      nextLink.getExtensions().setXmsSkipUrlEncoding(true);
      List<Parameter> requestParams = new ArrayList<>();
      requestParams.add(nextLink);
      nextOperation.getRequests().get(0).setParameters(requestParams);
      List<Parameter> signatureParams = new ArrayList<>();
      signatureParams.add(nextLink);
      nextOperation.getRequests().get(0).setSignatureParameters(signatureParams);
      nextOperation.setApiVersions(operation.getApiVersions());
      nextOperation.setDeprecated(operation.getDeprecated());
      nextOperation.setDescription(operation.getDescription());
      nextOperation.setExceptions(operation.getExceptions());
      nextOperation.setExtensions(operation.getExtensions());
      nextOperation.setExternalDocs(operation.getExternalDocs());
      nextOperation.setProfile(operation.getProfile());
      nextOperation.setResponses(operation.getResponses());
      nextOperation.setSummary(operation.getSummary());
      nextOperation.setUid(operation.getUid());

      if (operation.getExtensions().getXmsPageable().getOperationName() == null) {
        operation.getRequests().stream().flatMap(r -> r.getParameters().stream())
                .filter(parameter -> {
                  return parameter.getProtocol() == null || parameter.getProtocol().getHttp() == null
                          || (parameter.getProtocol().getHttp().getIn() != null
                          && (parameter.getProtocol().getHttp().getIn().equals(RequestParameterLocation.HEADER)
                          || parameter.getProtocol().getHttp().getIn().equals(RequestParameterLocation.URI)));
                })
                .forEach(param -> {
                  nextOperation.getRequests().get(0).getParameters().add(param);
                });

        operation.getRequests().stream().flatMap(r -> r.getSignatureParameters().stream())
                .filter(parameter -> {
                  return parameter.getProtocol() == null || parameter.getProtocol().getHttp() == null
                          || (parameter.getProtocol().getHttp().getIn() != null
                          && (parameter.getProtocol().getHttp().getIn().equals(RequestParameterLocation.HEADER)
                          || parameter.getProtocol().getHttp().getIn().equals(RequestParameterLocation.URI)));
                })
                .forEach(param -> {
                  nextOperation.getRequests().get(0).getSignatureParameters().add(param);
                });
      }
      operation.getExtensions().getXmsPageable().setNextOperation(nextOperation);
      nextOperation.getExtensions().getXmsPageable().setNextOperation(nextOperation);
      operationGroup.getOperations().add(nextOperation);
    } else {
      Operation nextOperation = operationGroup.getOperations().stream()
          .filter(o -> o.getLanguage().getJava().getName().equals(operationName))
          .findFirst().get();
      if (nextOperation.getExtensions() == null) {
        nextOperation.setExtensions(new XmsExtensions());
      }
      if (nextOperation.getExtensions().getXmsPageable() == null) {
        nextOperation.getExtensions().setXmsPageable(new XmsPageable());
      }
      operation.getExtensions().getXmsPageable().setNextOperation(nextOperation);
      nextOperation.getExtensions().getXmsPageable().setNextOperation(nextOperation);
    }
  }

  private void renameType(Metadata schema) {
    Language language = schema.getLanguage().getDefault();
    Language java = addJavaLanguage(schema);
    java.setName(CodeNamer.getTypeName(language.getName()));
    java.setSerializedName(language.getSerializedName());
    java.setDescription(language.getDescription());
    schema.getLanguage().setJava(java);
  }

  private void renameProperty(Property property) {
    Language language = property.getLanguage().getDefault();
    Language java = addJavaLanguage(property);
    java.setName(CodeNamer.getPropertyName(language.getName()));
    java.setSerializedName(language.getSerializedName());
    java.setDescription(language.getDescription());
    property.getLanguage().setJava(java);
  }

  private void renameCodeModel(CodeModel codeModel) {
    renameType(codeModel);
    if (codeModel.getLanguage().getJava().getName() == null
        || codeModel.getLanguage().getJava().getName().isEmpty()) {
      codeModel.getLanguage().getJava().setName(CodeNamer.getTypeName(codeModel.getInfo().getTitle()));
    }
  }

  private void renameVariable(Metadata schema) {
    Language language = schema.getLanguage().getDefault();
    Language java = addJavaLanguage(schema);
    java.setName(CodeNamer.getParameterName(language.getName()));
    java.setSerializedName(language.getSerializedName());
    java.setDescription(language.getDescription());
    schema.getLanguage().setJava(java);
  }

  private void renameMethodGroup(Metadata schema) {
    Language language = schema.getLanguage().getDefault();
    Language java = addJavaLanguage(schema);
    java.setName(CodeNamer.getMethodGroupName(language.getName()));
    java.setSerializedName(language.getSerializedName());
    java.setDescription(language.getDescription());
    schema.getLanguage().setJava(java);
  }

  private void renameMethod(Metadata schema) {
    Language language = schema.getLanguage().getDefault();
    Language java = addJavaLanguage(schema);
    java.setName(CodeNamer.getMethodName(language.getName()));
    java.setSerializedName(language.getSerializedName());
    java.setDescription(language.getDescription());
  }

  private Language addJavaLanguage(Metadata schema) {
    Language java = schema.getLanguage().getJava();
    if (java == null) {
      java = new Language();
      schema.getLanguage().setJava(java);
    }
    return java;
  }

  private Parameter createContentLengthParameter(Operation operation, Parameter bodyParam) {
    Parameter contentType = new Parameter();
    contentType.setOperation(operation);
    contentType.setDescription("The Content-Length header for the request");
    contentType.setRequired(bodyParam.isRequired());
    NumberSchema longSchema = new NumberSchema();
    longSchema.setPrecision(64);
    longSchema.setType(Schema.AllSchemaTypes.INTEGER);
    contentType.setSchema(longSchema);
    contentType.setImplementation(Parameter.ImplementationLocation.METHOD);
    contentType.setProtocol(new Protocols());
    contentType.getProtocol().setHttp(new Protocol());
    contentType.getProtocol().getHttp().setIn(RequestParameterLocation.HEADER);
    Language language = new Language();
    language.setName("contentLength");
    language.setSerializedName("Content-Length");
    language.setDescription("The Content-Length header for the request");
    contentType.setLanguage(new Languages());
    contentType.getLanguage().setDefault(language);
    contentType.getLanguage().setJava(language);
    return contentType;
  }

  private static void deduplicateParameterNames(Request request) {
    if (request == null || request.getParameters() == null || request.getParameters().isEmpty()) {
      return;
    }

    List<Parameter> parameters = request.getParameters();
    // remove duplicate item
    List<Parameter> deduplicatedParameters = parameters.stream()
        .distinct()
        .collect(Collectors.toList());
    if (deduplicatedParameters.size() < parameters.size()) {
      parameters = deduplicatedParameters;
      request.setParameters(parameters);
    }

    // rename if name conflict
    Set<String> parameterNames = new HashSet<>();
    ListIterator<Parameter> iter = parameters.listIterator();
    while (iter.hasNext()) {
      Parameter parameter = iter.next();
      if (parameter.getOriginalParameter() == null // skip the parameters resulted from parameter-flattening as they are not in proxy method
          && parameterNames.contains(parameter.getLanguage().getJava().getName())) {
        parameter.getLanguage().getJava().setName(parameter.getLanguage().getJava().getName() + "Param");
      }

      parameterNames.add(parameter.getLanguage().getJava().getName());
    }
  }

  private final static Map<String, String> ODATA_PARAMETER_NAME_CONVERSION = new HashMap<>(2);
  static {
    ODATA_PARAMETER_NAME_CONVERSION.put("maxpagesize", "maxPageSize");
    ODATA_PARAMETER_NAME_CONVERSION.put("orderby", "orderBy");
  }

  private static void renameOdataParameterNames(Request request) {
    List<Parameter> parameters = request.getParameters();
    ListIterator<Parameter> iter = parameters.listIterator();
    while (iter.hasNext()) {
      Parameter parameter = iter.next();
      if (parameter.getProtocol() != null && parameter.getProtocol().getHttp() != null
          && (parameter.getProtocol().getHttp().getIn() == RequestParameterLocation.QUERY
          || parameter.getProtocol().getHttp().getIn() == RequestParameterLocation.HEADER)) {
        String serializedName = parameter.getLanguage().getDefault().getSerializedName();
        String convertedName = ODATA_PARAMETER_NAME_CONVERSION.get(serializedName);
        if (convertedName != null
            // no x-ms-client-name
            && serializedName.equals(parameter.getLanguage().getJava().getName())) {
          parameter.getLanguage().getJava().setName(convertedName);
        }
      }
    }
  }
}
