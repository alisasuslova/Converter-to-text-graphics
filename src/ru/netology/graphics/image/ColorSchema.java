package ru.netology.graphics.image;

public class ColorSchema implements TextColorSchema{
    private final char[] defSchema = {'#', '$', '@', '%', '*', '+', '-', '"'};
    private char[] schema;

    @Override
    public char convert(int color) {
        return this.schema[color / 32];
    }

    public void setSchema(char[] userSchema) {
        this.schema = userSchema;
    }

    public ColorSchema() {
        this.schema = defSchema;
    }
}
