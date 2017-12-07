package processor;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import javax.annotation.processing.*;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.TypeElement;
import javax.lang.model.type.ExecutableType;
import javax.lang.model.type.MirroredTypeException;
import javax.lang.model.type.MirroredTypesException;
import javax.lang.model.type.TypeMirror;
import javax.tools.Diagnostic;
import javax.tools.JavaFileObject;

import annotations.BAO;
import annotations.BucketConfig;
import annotations.BucketEntity;
import com.google.auto.service.AutoService;
import com.squareup.javapoet.*;
import db.BucketDatabase;
import db.BucketDatabaseManager;

@SupportedSourceVersion(SourceVersion.RELEASE_8)
@AutoService(Processor.class)
public class BucketProcessor extends AbstractProcessor {
    private Filer filer;

    @Override
    public synchronized void init(ProcessingEnvironment processingEnv) {
        this.filer = processingEnv.getFiler();
    }

    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
        for (Element element :
                roundEnv.getElementsAnnotatedWith(BucketConfig.class)) {
            setupBucketConfigAnnotation(element);
        }

        return true;
    }

    @Override
    public Set<String> getSupportedAnnotationTypes() {
        Set<String> annotataions = new LinkedHashSet<String>();
        annotataions.add(BucketEntity.class.getCanonicalName());
        annotataions.add(BAO.class.getCanonicalName());
        annotataions.add(BucketConfig.class.getCanonicalName());
        return annotataions;
    }


    private void setupBucketConfigAnnotation(Element element) {




        String[] entities = null;

        try {
            element.getAnnotation(BucketConfig.class).entities();
        }catch (MirroredTypesException ex){
            List<TypeMirror> typeMirrors = (List<TypeMirror>) ex.getTypeMirrors();

            if(typeMirrors != null) {
                entities = new String[typeMirrors.size()];
                for (int i = 0; i < entities.length; i++) {
                    entities[i] = typeMirrors.get(i).toString() + ".class";
                }
            }

        }

        FieldSpec managerInstanceField = FieldSpec.builder(BucketDatabaseManager.class, "managerInstance", Modifier.PRIVATE, Modifier.STATIC).build();

        MethodSpec.Builder methodBuilder = MethodSpec.methodBuilder("getInstance")
                .addModifiers(Modifier.PUBLIC, Modifier.STATIC)
                .beginControlFlow("if(managerInstance == null)")
                .addCode("  managerInstance = $T\n" +
                        "                .newInstance()\n"
                        +"                .declare()\n", BucketDatabase.class);

        if (entities != null) {
            for (String entity :
                    entities) {
                methodBuilder.addCode("                .createCollection("+entity+")\n");
            }
        }


        methodBuilder.addCode(
                "                .end();\n", BucketDatabase.class)
                .endControlFlow()
                .addCode("return managerInstance;\n")
                .returns(BucketDatabaseManager.class)
                .build();


        TypeSpec bucketClass = TypeSpec.classBuilder(element.getSimpleName() + "Impl")
                .addField(managerInstanceField)
                .addModifiers(Modifier.PUBLIC,Modifier.FINAL)
                .addMethod(methodBuilder.build())
                .build();


        JavaFile javaFile = JavaFile.builder("buckets", bucketClass).build();


        try {
            javaFile.writeTo(filer);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private void writeBuilderFile(String className, Map<String, String> setterMap) throws IOException {

        String packageName = null;
        int lastDot = className.lastIndexOf('.');
        if (lastDot > 0) {
            packageName = className.substring(0, lastDot);
        }

        String simpleClassName = className.substring(lastDot + 1);
        String builderClassName = className + "Builder";
        String builderSimpleClassName = builderClassName.substring(lastDot + 1);

        JavaFileObject builderFile = processingEnv.getFiler().createSourceFile(builderClassName);
        try (PrintWriter out = new PrintWriter(builderFile.openWriter())) {

            if (packageName != null) {
                out.print("package ");
                out.print(packageName);
                out.println(";");
                out.println();
            }

            out.print("public class ");
            out.print(builderSimpleClassName);
            out.println(" {");
            out.println();

            out.print("    private ");
            out.print(simpleClassName);
            out.print(" object = new ");
            out.print(simpleClassName);
            out.println("();");
            out.println();

            out.print("    public ");
            out.print(simpleClassName);
            out.println(" build() {");
            out.println("        return object;");
            out.println("    }");
            out.println();

            setterMap.entrySet().forEach(setter -> {
                String methodName = setter.getKey();
                String argumentType = setter.getValue();

                out.print("    public ");
                out.print(builderSimpleClassName);
                out.print(" ");
                out.print(methodName);

                out.print("(");

                out.print(argumentType);
                out.println(" value) {");
                out.print("        object.");
                out.print(methodName);
                out.println("(value);");
                out.println("        return this;");
                out.println("    }");
                out.println();
            });

            out.println("}");

        }
    }

}