package net.clydo.clytil.reflect;

import lombok.experimental.UtilityClass;
import lombok.val;
import net.clydo.clytil.Validates;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.lang.annotation.Annotation;
import java.lang.reflect.AnnotatedElement;

@UtilityClass
public class Annotations {

    public <A extends Annotation> A require(
            @NotNull final AnnotatedElement element,
            @NotNull final Class<A> annotationClass
    ) {
        Validates.requireParam(element, "element");
        Validates.requireParam(annotationClass, "annotationClass");

        return require(element, annotationClass, true);
    }

    public <A extends Annotation> A require(
            @NotNull AnnotatedElement element,
            @NotNull Class<A> annotationClass,
            boolean inRoot
    ) {
        Validates.requireParam(element, "element");
        Validates.requireParam(annotationClass, "annotationClass");

        val annotation = get(element, annotationClass, inRoot);
        if (annotation == null) {
            throwNotAnnotated(element, annotationClass);
        }
        return annotation;
    }

    @Contract("_, _ -> fail")
    public <A extends Annotation> void throwNotAnnotated(@NotNull AnnotatedElement element, @NotNull Class<A> annotationClass) {
        throw new IllegalStateException(element + " is not annotated with @" + annotationClass.getSimpleName());
    }

    public <A extends Annotation> A get(
            @NotNull final AnnotatedElement element,
            @NotNull final Class<A> annotationClass
    ) {
        Validates.requireParam(element, "element");
        Validates.requireParam(annotationClass, "annotationClass");

        return get(element, annotationClass, false);
    }

    public <A extends Annotation> A getRecursive(
            @NotNull final AnnotatedElement element,
            @NotNull final Class<A> annotationClass
    ) {
        Validates.requireParam(element, "element");
        Validates.requireParam(annotationClass, "annotationClass");

        return get(element, annotationClass, true);
    }

    public <A extends Annotation> A get(
            @NotNull final AnnotatedElement element,
            @NotNull final Class<A> annotationClass,
            final boolean recursive
    ) {
        Validates.requireParam(element, "element");
        Validates.requireParam(annotationClass, "annotationClass");

        val direct = element.getAnnotation(annotationClass);
        if (direct != null || recursive) {
            return direct;
        }

        val declaredAnnotations = element.getDeclaredAnnotations();
        for (Annotation declaredAnnotation : declaredAnnotations) {
            val meta = get(declaredAnnotation.annotationType(), annotationClass, true);
            if (meta == null) {
                continue;
            }

            return meta;
        }
        return null;
    }

    @SuppressWarnings("unchecked")
    public <A extends Annotation> A getByMeta(
            @NotNull final AnnotatedElement element,
            @NotNull final Class<? extends Annotation> metaAnnotationClass
    ) {
        Validates.requireParam(element, "element");
        Validates.requireParam(metaAnnotationClass, "metaAnnotationClass");

        for (Annotation annotation : element.getAnnotations()) {
            val annotationType = annotation.annotationType();
            if (!annotationType.isAnnotationPresent(metaAnnotationClass)) {
                continue;
            }

            return (A) annotation;
        }

        return null;
    }

}
