package org.hyperskill.community.flashcards.validation;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.RecordComponent;
import java.util.Arrays;
import java.util.Set;
import java.util.function.Supplier;

public class JpaUnitTestValidator<T> {

    private final Validator validator;
    private final Supplier<? extends T> getValidFunction;
    private Constructor<? extends T> recordConstructor;

    public JpaUnitTestValidator(Supplier<? extends T> getValidObjectFunction, Class<? extends T> dtoClass) {
        try (var validatorFactory = Validation.buildDefaultValidatorFactory()) {
            this.validator = validatorFactory.getValidator();
        }
        this.getValidFunction = getValidObjectFunction;
        initRecordConstructorIfNeeded(dtoClass);
    }

    private void initRecordConstructorIfNeeded(Class<? extends T> dtoClass) {
        if (dtoClass.isRecord()) {
            Class<?>[] componentTypes = Arrays.stream(dtoClass.getRecordComponents())
                    .map(RecordComponent::getType)
                    .toArray(Class<?>[]::new);
            try {
                recordConstructor = dtoClass.getDeclaredConstructor(componentTypes);
            } catch (NoSuchMethodException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public Set<ConstraintViolation<T>> getConstraintViolationsOnUpdate(String fieldToUpdate, Object newValue)
            throws Exception {
        T object = modifyDto(getValidFunction.get(), fieldToUpdate, newValue);
        return validator.validate(object);
    }

    private T modifyDto(T dto, String fieldName, Object value) throws Exception {
        if (recordConstructor != null) {
            return createModifiedRecord(dto, fieldName, value);
        } else {
            return updateFieldByReflection(dto, fieldName, value);
        }
    }

    private T updateFieldByReflection(T dto, String fieldName, Object value) throws Exception {
        Field field = dto.getClass().getDeclaredField(fieldName);
        field.setAccessible(true);
        field.set(dto, value);
        return dto;
    }

    private T createModifiedRecord(T dto, String fieldName, Object value) throws Exception {
        var recordComponents = dto.getClass().getRecordComponents();
        Object[] modifiedValues = new Object[recordComponents.length];
        for (int i = 0; i < recordComponents.length; i++) {
            modifiedValues[i] = recordComponents[i].getName().equals(fieldName)
                    ? value : recordComponents[i].getAccessor().invoke(dto);
        }
        return recordConstructor.newInstance(modifiedValues);
    }
}
