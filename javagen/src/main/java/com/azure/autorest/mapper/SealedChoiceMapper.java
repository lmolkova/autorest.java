// Copyright (c) Microsoft Corporation. All rights reserved.
// Licensed under the MIT License.

package com.azure.autorest.mapper;

import com.azure.autorest.extension.base.model.codemodel.ChoiceValue;
import com.azure.autorest.extension.base.model.codemodel.SealedChoiceSchema;
import com.azure.autorest.extension.base.plugin.JavaSettings;
import com.azure.autorest.model.clientmodel.ClassType;
import com.azure.autorest.model.clientmodel.ClientEnumValue;
import com.azure.autorest.model.clientmodel.EnumType;
import com.azure.autorest.model.clientmodel.IType;
import com.azure.autorest.model.clientmodel.ImplementationDetails;
import com.azure.autorest.util.CodeNamer;
import com.azure.autorest.util.SchemaUtil;
import com.azure.core.util.CoreUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class SealedChoiceMapper implements IMapper<SealedChoiceSchema, IType> {
    private static final SealedChoiceMapper INSTANCE = new SealedChoiceMapper();
    Map<SealedChoiceSchema, IType> parsed = new ConcurrentHashMap<>();

    private SealedChoiceMapper() {
    }

    public static SealedChoiceMapper getInstance() {
        return INSTANCE;
    }

    @Override
    public IType map(SealedChoiceSchema enumType) {
        if (enumType == null) {
            return null;
        }

        IType sealedChoiceType = parsed.get(enumType);
        if (sealedChoiceType != null) {
            return sealedChoiceType;
        }

        sealedChoiceType = createSealedChoiceType(enumType);
        parsed.put(enumType, sealedChoiceType);

        return sealedChoiceType;
    }

    private IType createSealedChoiceType(SealedChoiceSchema enumType) {
        JavaSettings settings = JavaSettings.getInstance();
        String enumTypeName = enumType.getLanguage().getJava().getName();

        if (enumTypeName == null || enumTypeName.isEmpty() || enumTypeName.equals("enum")) {
            return ClassType.String;
        } else {
            String enumSubpackage = settings.getModelsSubpackage();
            if (settings.isCustomType(enumTypeName)) {
                enumSubpackage = settings.getCustomTypesSubpackage();
            }
            String enumPackage = settings.getPackage(enumSubpackage);

            String summary = enumType.getSummary();
            String description = enumType.getLanguage().getJava() == null ? null : enumType.getLanguage().getJava().getDescription();
            description = SchemaUtil.mergeSummaryWithDescription(summary, description);
            if (CoreUtils.isNullOrEmpty(description)) {
                description = "Defines values for " + enumTypeName + ".";
            }

            List<ClientEnumValue> enumValues = new ArrayList<>();
            for (ChoiceValue enumValue : enumType.getChoices()) {
                String enumName = enumValue.getValue();
                if (!settings.isFluent()) {
                    if (enumValue.getLanguage() != null && enumValue.getLanguage().getJava() != null
                        && enumValue.getLanguage().getJava().getName() != null) {
                        enumName = enumValue.getLanguage().getJava().getName();
                    } else if (enumValue.getLanguage() != null && enumValue.getLanguage().getDefault() != null
                        && enumValue.getLanguage().getDefault().getName() != null) {
                        enumName = enumValue.getLanguage().getDefault().getName();
                    }
                }
                String memberName = CodeNamer.getEnumMemberName(enumName);
                enumValues.add(new ClientEnumValue(memberName, enumValue.getValue()));
            }

            return new EnumType.Builder()
                .packageName(enumPackage)
                .name(enumTypeName)
                .description(description)
                .expandable(false)
                .values(enumValues)
                .elementType(Mappers.getSchemaMapper().map(enumType.getChoiceType()))
                .implementationDetails(new ImplementationDetails.Builder()
                    .usages(SchemaUtil.mapSchemaContext(enumType.getUsage()))
                    .build())
                .build();
        }
    }
}
