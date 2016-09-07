package io.github.yangxlei.compile;

import com.google.auto.service.AutoService;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.TypeSpec;

import java.io.IOException;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.Processor;
import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.TypeElement;
import javax.lang.model.util.Elements;

import io.github.yangxlei.annotation.ActionType;

/**
 * Created by yanglei on 16/9/3.
 */
@AutoService(Processor.class)
public class ActionProcessor extends AbstractProcessor {

    private Elements elementUtils;

    @Override
    public Set<String> getSupportedAnnotationTypes() {
        return Collections.singleton(ActionType.class.getCanonicalName());
    }

    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
        Set<? extends Element> elements = roundEnv.getElementsAnnotatedWith(ActionType.class);

        TypeSpec.Builder builder = TypeSpec.classBuilder("ModuleActionTypes");
        builder.addModifiers(Modifier.FINAL, Modifier.PUBLIC);

        MethodSpec.Builder methodBuilder = MethodSpec.methodBuilder("getModuleActionTypes")
                .addModifiers(Modifier.PUBLIC, Modifier.STATIC, Modifier.FINAL)
                .addStatement("$T map = new $T()", Map.class, HashMap.class)
                .returns(Map.class);

        String packageName = null;

        for (Element element : elements) {
            TypeElement typeElement = (TypeElement) element;
            ActionType action = typeElement.getAnnotation(ActionType.class);
            if (action == null || action.value() == null) continue;

            methodBuilder.addStatement("map.put($S, new $T())", action.value(), typeElement.asType());

            packageName = getPackageName(typeElement);
        }

        if (packageName == null) return false;

        methodBuilder.addStatement("return map");
        builder.addMethod(methodBuilder.build());
        JavaFile javaFile = JavaFile.builder(packageName, builder.build())
                .build();
        try {
            javaFile.writeTo(processingEnv.getFiler());
        } catch (IOException e) {
            e.printStackTrace();
        }

        return true;
    }

    private String getPackageName(TypeElement type) {
        return elementUtils.getPackageOf(type).getQualifiedName().toString();
    }

    @Override
    public synchronized void init(ProcessingEnvironment processingEnv) {
        super.init(processingEnv);
        elementUtils = processingEnv.getElementUtils();
    }

    @Override
    public SourceVersion getSupportedSourceVersion() {
        return SourceVersion.RELEASE_7;
    }
}
