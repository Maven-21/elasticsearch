/*
 * Copyright Elasticsearch B.V. and/or licensed to Elasticsearch B.V. under one
 * or more contributor license agreements. Licensed under the Elastic License;
 * you may not use this file except in compliance with the Elastic License.
 */
package org.elasticsearch.xpack.ml.job.config;

import org.elasticsearch.common.ParseField;
import org.elasticsearch.common.io.stream.StreamInput;
import org.elasticsearch.common.io.stream.StreamOutput;
import org.elasticsearch.common.io.stream.Writeable;
import org.elasticsearch.xpack.ml.job.messages.Messages;

import java.io.IOException;
import java.util.EnumSet;
import java.util.Locale;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Enum representing logical comparisons on doubles
 */
public enum Operator implements Writeable {
    EQ {
        @Override
        public boolean test(double lhs, double rhs) {
            return Double.compare(lhs, rhs) == 0;
        }
    },
    GT {
        @Override
        public boolean test(double lhs, double rhs) {
            return Double.compare(lhs, rhs) > 0;
        }
    },
    GTE {
        @Override
        public boolean test(double lhs, double rhs) {
            return Double.compare(lhs, rhs) >= 0;
        }
    },
    LT {
        @Override
        public boolean test(double lhs, double rhs) {
            return Double.compare(lhs, rhs) < 0;
        }
    },
    LTE {
        @Override
        public boolean test(double lhs, double rhs) {
            return Double.compare(lhs, rhs) <= 0;
        }
    },
    MATCH {
        @Override
        public boolean match(Pattern pattern, String field) {
            Matcher match = pattern.matcher(field);
            return match.matches();
        }

        @Override
        public boolean expectsANumericArgument() {
            return false;
        }
    };

    public static final ParseField OPERATOR_FIELD = new ParseField("operator");

    public boolean test(double lhs, double rhs) {
        return false;
    }

    public boolean match(Pattern pattern, String field) {
        return false;
    }

    public boolean expectsANumericArgument() {
        return true;
    }

    public static Operator fromString(String name) {
        return valueOf(name.trim().toUpperCase(Locale.ROOT));
    }

    public static Operator readFromStream(StreamInput in) throws IOException {
        int ordinal = in.readVInt();
        if (ordinal < 0 || ordinal >= values().length) {
            throw new IOException("Unknown Operator ordinal [" + ordinal + "]");
        }
        return values()[ordinal];
    }

    @Override
    public void writeTo(StreamOutput out) throws IOException {
        out.writeVInt(ordinal());
    }

    @Override
    public String toString() {
        return name().toLowerCase(Locale.ROOT);
    }
}
