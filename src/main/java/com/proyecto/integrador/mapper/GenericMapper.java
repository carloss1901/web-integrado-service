package com.proyecto.integrador.mapper;

import org.springframework.stereotype.Component;

import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Component
public class GenericMapper {

    public <P, R> R toResponse(P projection, Class<R> responseClass) {
        try {
            R response = responseClass.getDeclaredConstructor().newInstance();
            mapProperties(projection, response);
            return response;
        } catch (ReflectiveOperationException | IntrospectionException e) {
            throw new IllegalStateException("No se pudo mapear la proyeccion a response", e);
        }
    }

    public <P, R> List<R> toResponseList(List<P> projections, Class<R> responseClass) {
        return projections.stream()
            .map(projection -> toResponse(projection, responseClass))
            .toList();
    }

    private <P, R> void mapProperties(P projection, R response)
        throws IntrospectionException, InvocationTargetException, IllegalAccessException {

        Map<String, PropertyDescriptor> projectionProperties = Arrays.stream(
                Introspector.getBeanInfo(projection.getClass()).getPropertyDescriptors())
            .filter(property -> property.getReadMethod() != null)
            .collect(Collectors.toMap(PropertyDescriptor::getName, Function.identity(), (first, second) -> first));

        for (PropertyDescriptor responseProperty : Introspector.getBeanInfo(response.getClass()).getPropertyDescriptors()) {
            PropertyDescriptor projectionProperty = projectionProperties.get(responseProperty.getName());
            if (projectionProperty != null && responseProperty.getWriteMethod() != null) {
                Object value = projectionProperty.getReadMethod().invoke(projection);
                responseProperty.getWriteMethod().invoke(response, value);
            }
        }
    }
}
