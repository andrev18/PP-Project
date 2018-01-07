package processor;

import java.io.IOException;
import java.lang.reflect.ParameterizedType;
import java.util.*;
import javax.annotation.processing.*;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.TypeElement;
import javax.lang.model.type.DeclaredType;
import javax.lang.model.type.MirroredTypesException;
import javax.lang.model.type.TypeMirror;
import javax.tools.Diagnostic;

import annotations.*;
import db.BaseBao;
import com.google.auto.service.AutoService;
import com.squareup.javapoet.*;
import db.BucketDatabase;
import db.BucketDatabaseManager;


/**
 * Annotation processor for configurating Bucket Files
 */
@SupportedSourceVersion(SourceVersion.RELEASE_8)
@AutoService(Processor.class)
public class BucketProcessor extends AbstractProcessor {
    private Filer filer;
    private Element bucketConfiguration;
    private Set<? extends Element> baos;


    @Override
    public synchronized void init(ProcessingEnvironment processingEnv) {
        this.filer = processingEnv.getFiler();
        this.processingEnv = processingEnv;
    }

    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
        for (Element element : roundEnv.getElementsAnnotatedWith(BucketConfig.class)) {
            if (bucketConfiguration == null) {
                bucketConfiguration = element;
            } else {
                processingEnv.getMessager().printMessage(Diagnostic.Kind.ERROR, "There can't be more than 1 BucketConfiguration per project. Found in ", element);
            }
        }

        baos = roundEnv.getElementsAnnotatedWith(BAO.class);

        setupBucketConfigAnnotation();

        return true;
    }


    @Override
    public Set<String> getSupportedAnnotationTypes() {
        Set<String> annotataions = new LinkedHashSet<String>();
        annotataions.add(BAO.class.getCanonicalName());
        annotataions.add(BucketConfig.class.getCanonicalName());
        return annotataions;
    }





    private void setupBucketConfigAnnotation() {

        String[] entities = null;

        try {
            bucketConfiguration.getAnnotation(BucketConfig.class).entities();
        } catch (MirroredTypesException ex) {
            List<TypeMirror> typeMirrors = (List<TypeMirror>) ex.getTypeMirrors();

            if (typeMirrors != null) {
                entities = new String[typeMirrors.size()];
                for (int i = 0; i < entities.length; i++) {
                    entities[i] = typeMirrors.get(i).toString() + ".class";
                }
            }

        }

        FieldSpec managerInstanceField = FieldSpec.builder(BucketDatabaseManager.class, "bucketManager", Modifier.PRIVATE, Modifier.FINAL).build();
        FieldSpec singletonInstance = FieldSpec.builder(ClassName.bestGuess("AppBucketImpl"), "instance", Modifier.PRIVATE, Modifier.STATIC).build();



        List<FieldSpec> baosFields = new ArrayList<>();
        List<MethodSpec> baosGetters = Collections.emptyList();


        MethodSpec.Builder mainConstructor = MethodSpec.constructorBuilder()
                .addModifiers(Modifier.PRIVATE)
                .addCode("  bucketManager = $T\n" +
                        "                .newInstance()\n"
                        + "                .declare()\n", BucketDatabase.class);

        if (entities != null) {
            for (String entity : entities) {
                mainConstructor.addCode("                .createCollection(" + entity + ")\n");
            }
        }

        mainConstructor.addCode("                .end();\n", BucketDatabase.class);

        for (Element baoElement:baos) {

            ((DeclaredType)((TypeElement) baoElement).getSuperclass()).getTypeArguments();
            String modelClassName  = ((DeclaredType)((TypeElement) baoElement).getSuperclass()).getTypeArguments().get(0).toString();
            String baoClassName = ((TypeElement) baoElement).getQualifiedName().toString();
            String baoFieldName = ((TypeElement) baoElement).getSimpleName().toString();
            baoFieldName = baoFieldName.substring(0,1).toLowerCase() + baoFieldName.substring(1);

            FieldSpec baoFieldSpec = FieldSpec.builder(ClassName.bestGuess(baoClassName),baoFieldName,Modifier.PUBLIC).build();

            mainConstructor.addCode(baoFieldSpec.name + " = new "+baoFieldSpec.type+"(bucketManager,"+modelClassName+".class);\n");
            baosFields.add(baoFieldSpec);
        }


        MethodSpec.Builder methodInstanceBuilder = MethodSpec.methodBuilder("getInstance")
                .addModifiers(Modifier.STATIC,Modifier.PUBLIC)
                .beginControlFlow("if(instance == null)")
                .addCode("  instance = new AppBucketImpl();\n")
                .endControlFlow()
                .addCode("return instance;\n")
                .returns(ClassName.bestGuess("AppBucketImpl"));



        TypeSpec bucketClass = TypeSpec.classBuilder(bucketConfiguration.getSimpleName() + "Impl")
                .addField(managerInstanceField)
                .addField(singletonInstance)
                .addFields(baosFields)
                .addModifiers(Modifier.PUBLIC, Modifier.FINAL)
                .addMethod(mainConstructor.build())
                .addMethod(methodInstanceBuilder.build())
                .build();


        JavaFile javaFile = JavaFile.builder("buckets", bucketClass).build();


        try {
            javaFile.writeTo(filer);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

}