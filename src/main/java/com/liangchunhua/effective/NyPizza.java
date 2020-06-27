package com.liangchunhua.effective;

import java.util.Objects;

public class NyPizza extends Pizza {
    public enum Size {
        SMALL, MEDUIM, LARGE
    };

    private final Size size;

    public static class Builder extends Pizza.Builder<Builder> {
        private final Size size;

        public Builder(final Size size) {
            this.size = Objects.requireNonNull(size);
        }

        @Override
        NyPizza build() {
            return new NyPizza(this);
        }

        @Override
        protected Builder self() {
            return this;
        }

    }

    private NyPizza(Builder builder) {
        super(builder);
        size = builder.size;
    }

}