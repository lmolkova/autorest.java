// Copyright (c) Microsoft Corporation. All rights reserved.
// Licensed under the MIT License.

package com.azure.autorest.mapper;

import com.azure.autorest.extension.base.model.codemodel.ObjectSchema;
import com.azure.autorest.extension.base.model.codemodel.OrSchema;
import com.azure.autorest.extension.base.model.codemodel.Property;
import com.azure.autorest.extension.base.model.codemodel.Schema;
import com.azure.autorest.model.clientmodel.ClassType;
import com.azure.autorest.model.clientmodel.ClientModelProperty;
import com.azure.autorest.model.clientmodel.ImplementationDetails;
import com.azure.autorest.model.clientmodel.UnionModel;
import com.azure.autorest.model.clientmodel.UnionModels;
import com.azure.autorest.util.SchemaUtil;
import com.azure.core.util.CoreUtils;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class UnionModelMapper implements IMapper<OrSchema, List<UnionModel>> {

    private static final UnionModelMapper INSTANCE = new UnionModelMapper();
    private final UnionModels serviceModels = UnionModels.getInstance();

    protected UnionModelMapper() {
    }

    public static UnionModelMapper getInstance() {
        return INSTANCE;
    }

    @Override
    public List<UnionModel> map(OrSchema type) {
        ClassType baseModelType = Mappers.getUnionMapper().map(type);
        String baseModelName = baseModelType.getName();
        List<UnionModel> models = serviceModels.getModel(baseModelType.getName());
        if (models == null) {
            models = new ArrayList<>();

            // superclass
            UnionModel.Builder builder = new UnionModel.Builder()
                    .name(baseModelName)
                    .packageName(baseModelType.getPackage())
                    .implementationDetails(new ImplementationDetails.Builder()
                            .usages(SchemaUtil.mapSchemaContext(type.getUsage()))
                            .build());
            processDescription(builder, type);

            models.add(builder.build());

            // subclasses
            for (ObjectSchema subtype : type.getAnyOf()) {
                String name = subtype.getLanguage().getJava().getName();
                builder.name(name)
                        .parentModelName(baseModelName);
                processDescription(builder, subtype);

                // import
                Set<String> imports = new HashSet<>();
                imports.add(baseModelType.getFullName());
                builder.imports(new ArrayList<>(imports));

                // property
                List<ClientModelProperty> properties = new ArrayList<>();
                for (Property property : subtype.getProperties()) {
                    ClientModelProperty modelProperty = Mappers.getModelPropertyMapper().map(property);
                    properties.add(modelProperty);
                }
                builder.properties(properties);

                models.add(builder.build());
            }

            serviceModels.addModel(models);
        }
        return models;
    }

    private static void processDescription(UnionModel.Builder builder, Schema type) {
        String summary = type.getSummary();
        String description = type.getLanguage().getJava() == null ? null : type.getLanguage().getJava().getDescription();
        if (CoreUtils.isNullOrEmpty(summary) && CoreUtils.isNullOrEmpty(description)) {
            builder.description(String.format("The %s model.", type.getLanguage().getJava().getName()));
        } else {
            builder.description(SchemaUtil.mergeSummaryWithDescription(summary, description));
        }
    }
}
