package org.mobilitydata.gtfsvalidator.util;

/*
 * Copyright (c) 2019. MobilityData IO. All rights reserved
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import com.google.common.base.Strings;
import org.apache.commons.validator.routines.FloatValidator;
import org.apache.commons.validator.routines.IntegerValidator;
import org.apache.commons.validator.routines.UrlValidator;
import org.jetbrains.annotations.NotNull;
import org.mobilitydata.gtfsvalidator.model.OccurrenceModel;

import javax.annotation.Nullable;
import java.time.ZoneId;
import java.util.List;
import java.util.Locale;
import java.util.regex.Pattern;

import static org.mobilitydata.gtfsvalidator.rules.ValidationRules.*;

public class GTFSTypeValidationUtils {

    public static @Nullable
    Float parseAndValidateFloat(@NotNull String validatedEntityId,
                                @NotNull String fieldName,
                                @Nullable String rawValue,
                                boolean canBeNullOrEmpty,
                                boolean canBeNegative,
                                @NotNull  List<OccurrenceModel> outList) {

        if (Strings.isNullOrEmpty(rawValue)) {
            if (!canBeNullOrEmpty) {
                RuleUtils.addOccurrence(E002, formatOccurrencePrefix(validatedEntityId, fieldName, "null or empty"), outList);
            }
            return null;
        }

        FloatValidator floatValidator = new FloatValidator();

        //FIXME: retrieve locale from agency_lang in agency.txt and if that doesn't exist,
        //from feed_lang in feed_info.txt before defaulting to Locale.US
        Float value = floatValidator.validate(rawValue, Locale.US);

        if (value == null || Float.isNaN(value)) {
            RuleUtils.addOccurrence(E003, formatOccurrencePrefix(validatedEntityId, fieldName, rawValue), outList);
            return null;
        }

        if (!canBeNegative && !floatValidator.minValue(value, 0)) {
            RuleUtils.addOccurrence(E004, formatOccurrencePrefix(validatedEntityId, fieldName, rawValue), outList);
            return null;
        }

        return value;
    }

    public static @Nullable
    Float parseAndValidateLatitude(@NotNull String validatedEntityId,
                                   @NotNull String fieldName,
                                   @Nullable String rawValue,
                                   boolean canBeNullOrEmpty,
                                   @NotNull List<OccurrenceModel> outList) {
        Float value = parseAndValidateFloat(validatedEntityId,
                fieldName,
                rawValue,
                canBeNullOrEmpty,
                true,
                outList);

        if (value == null) {
            return null;
        }

        FloatValidator floatValidator = new FloatValidator();

        if (!floatValidator.isInRange(value, -90f, 90f)) {
            //noinspection ConstantConditions
            RuleUtils.addOccurrence(E008, formatOccurrencePrefix(validatedEntityId, fieldName, rawValue), outList);
            return null;
        }

        return value;
    }

    public static @Nullable
    Float parseAndValidateLongitude(@NotNull String validatedEntityId,
                                    @NotNull String fieldName,
                                    @Nullable String rawValue,
                                    boolean canBeNullOrEmpty,
                                    @NotNull List<OccurrenceModel> outList) {
        Float value = parseAndValidateFloat(validatedEntityId,
                fieldName,
                rawValue,
                canBeNullOrEmpty,
                true,
                outList);

        if (value == null) {
            return null;
        }

        FloatValidator floatValidator = new FloatValidator();

        if (!floatValidator.isInRange(value, -180f, 180f)) {
            //noinspection ConstantConditions
            RuleUtils.addOccurrence(E009, formatOccurrencePrefix(validatedEntityId, fieldName, rawValue), outList);
            return null;
        }

        return value;
    }

    public static @Nullable
    Integer parseAndValidateInteger(@NotNull String validatedEntityId,
                                    @NotNull  String fieldName,
                                    @Nullable String rawValue,
                                    boolean canBeNullOrEmpty,
                                    boolean canBeNegative,
                                    @NotNull  List<OccurrenceModel> outList) {

        if (Strings.isNullOrEmpty(rawValue)) {
            if (!canBeNullOrEmpty) {
                RuleUtils.addOccurrence(E002, formatOccurrencePrefix(validatedEntityId, fieldName, "null or empty"), outList);
            }
            return null;
        }

        IntegerValidator integerValidator = new IntegerValidator();

        //FIXME: retrieve locale from agency_lang in agency.txt and if that doesn't exist,
        //from feed_lang in feed_info.txt before defaulting to Locale.US
        Integer value = integerValidator.validate(rawValue, Locale.US);

        if (value == null) {
            RuleUtils.addOccurrence(E005, formatOccurrencePrefix(validatedEntityId, fieldName, rawValue), outList);
            return null;
        }

        if (!canBeNegative && !integerValidator.minValue(value, 0)) {
            RuleUtils.addOccurrence(E006, formatOccurrencePrefix(validatedEntityId, fieldName, rawValue), outList);
            return null;
        }

        return value;
    }

    public static @Nullable
    String validateId(@NotNull String fieldName,
                      @Nullable String rawValue,
                      boolean canBeNullOrEmpty,
                      @NotNull List<OccurrenceModel> outList) {

        return validateString(Strings.isNullOrEmpty(rawValue) ? "" : rawValue,
                fieldName,
                rawValue,
                canBeNullOrEmpty,
                true,
                outList);
    }

    public static @Nullable
    String validateText(@NotNull String validatedEntityId,
                        @NotNull String fieldName,
                        @Nullable String rawValue,
                        boolean canBeNullOrEmpty,
                        @NotNull List<OccurrenceModel> outList) {

        return validateString(validatedEntityId,
                fieldName,
                rawValue,
                canBeNullOrEmpty,
                false,
                outList);
    }

    public static @Nullable
    String validateUrl(@NotNull String validatedEntityId,
                       @NotNull String fieldName,
                       @Nullable String rawValue,
                       boolean canBeNullOrEmpty,
                       @NotNull List<OccurrenceModel> outList) {

        if (Strings.isNullOrEmpty(rawValue)) {
            if (!canBeNullOrEmpty) {
                RuleUtils.addOccurrence(E002, formatOccurrencePrefix(validatedEntityId, fieldName, "null or empty"), outList);
            }
            return null;
        }

        UrlValidator urlValidator = new UrlValidator(VALID_URL_SCHEMES);

        if (!urlValidator.isValid(rawValue)) {
            RuleUtils.addOccurrence(E011, formatOccurrencePrefix(validatedEntityId, fieldName, rawValue), outList);
            return null;
        }

        return rawValue;
    }

    public static @Nullable
    String parseAndValidateColor(@NotNull String validatedEntityId,
                                 @NotNull String fieldName,
                                 @Nullable String rawValue,
                                 @NotNull List<OccurrenceModel> outList) {

        if (Strings.isNullOrEmpty(rawValue)) {
            return null;
        }

        if (!COLOR_6_DIGITS_HEXADECIMAL_PATTERN.matcher(rawValue).matches()) {
            RuleUtils.addOccurrence(E007, formatOccurrencePrefix(validatedEntityId, fieldName, rawValue), outList);
            return null;
        }

        return rawValue;
    }

    public static @Nullable
    String parseAndValidateTimeZone(@NotNull String validatedEntityId,
                                    @NotNull String fieldName,
                                    @Nullable String rawValue,
                                    @NotNull List<OccurrenceModel> outList) {

        if (Strings.isNullOrEmpty(rawValue)) {
            return null;
        }

        // Uses IANA timezone database shipped with JDK
        // to update without updating JDK see https://www.oracle.com/technetwork/java/javase/tzupdater-readme-136440.html
        if (!ZoneId.getAvailableZoneIds().contains(rawValue)) {
            RuleUtils.addOccurrence(E010, formatOccurrencePrefix(validatedEntityId, fieldName, rawValue), outList);
            return null;
        }

        return rawValue;
    }

    public static @Nullable
    String validateTime(@NotNull String validatedEntityId,
                        @NotNull String fieldName,
                        @Nullable String rawValue,
                        @NotNull List<OccurrenceModel>outList) {

        if(Strings.isNullOrEmpty(rawValue)) {
            return null;
        }

        if (!TIME_PATTERN.matcher(rawValue).matches()) {
            RuleUtils.addOccurrence(E012, formatOccurrencePrefix(validatedEntityId, fieldName, rawValue), outList);
            return null;
        }

        if (TIME_PATTERN_SUSPICIOUS.matcher(rawValue).matches()) {
            RuleUtils.addOccurrence(W002, formatOccurrencePrefix(validatedEntityId, fieldName, rawValue), outList);
        }

        return rawValue;
    }


    private static @Nullable
    String validateString(@NotNull String validatedEntityId,
                          @NotNull String fieldName,
                          @Nullable String rawValue,
                          boolean canBeNullOrEmpty,
                          boolean onlyPrintableAscii,
                          @NotNull List<OccurrenceModel> outList) {

        if (Strings.isNullOrEmpty(rawValue)) {
            if (!canBeNullOrEmpty) {
                RuleUtils.addOccurrence(E002, formatOccurrencePrefix(validatedEntityId, fieldName, "null or empty"), outList);
            }
            return null;
        }

        if (onlyPrintableAscii) {
            int charCount = rawValue.length();
            for (int i = 0; i < charCount; ++i) {
                if (!isPrintableAscii(rawValue.charAt(i))) {
                    RuleUtils.addOccurrence(W001, formatOccurrencePrefix(validatedEntityId, fieldName, rawValue), outList);
                    break;
                }
            }
        }

        return rawValue;
    }

    private static boolean isPrintableAscii(char ch) {
        return ch >= 32 && ch < 127;
    }
    private static final Pattern COLOR_6_DIGITS_HEXADECIMAL_PATTERN = Pattern.compile("[0-9A-Fa-f]{6}$");
    // General Time pattern HH:MM:SS or H:MM:SS
    private static final Pattern TIME_PATTERN = Pattern.compile("([0-9][0-9]|[0-9]):[0-5][0-9]:[0-5][0-9]");
    // By default, a warning is emitted for time >= 27:00:00
    //TODO: fine tune threshold value based on 95th percentile of real data
    //TODO: make it configurable through command line argument
    private static final Pattern TIME_PATTERN_SUSPICIOUS = Pattern.compile("[2-9][7-9]:[0-5][0-9]:[0-5][0-9]");
    private static final String[] VALID_URL_SCHEMES = { "http", "https" };
    private static String formatOccurrencePrefix(@NotNull String validatedEntityId,
                                                 @NotNull String fieldName,
                                                 @NotNull String rawValue) {
        return validatedEntityId + " " + fieldName + " is " + rawValue;
    }
}