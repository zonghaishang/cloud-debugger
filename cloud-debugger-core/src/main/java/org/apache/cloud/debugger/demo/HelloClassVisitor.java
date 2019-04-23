package org.apache.cloud.debugger.demo;

import org.objectweb.asm.AnnotationVisitor;
import org.objectweb.asm.Attribute;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.FieldVisitor;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.TypePath;

import java.util.LinkedHashSet;
import java.util.Set;

/**
 * @author yiji@apache.org
 */
public class HelloClassVisitor extends ClassVisitor {

    private String name;

    private boolean isInterface;

    private boolean isAnnotation;

    private boolean isAbstract;

    private boolean isFinal;

    private String enclosingClassName;

    private boolean independentInnerClass;

    private String superClassName;

    private String[] interfaces;

    private Set<String> memberClassNames = new LinkedHashSet<String>();

    public HelloClassVisitor(int api) {
        super(api);
    }

    /**
     * Visits the header of the class.
     *
     * @param version    the class version. The minor version is stored in the 16 most significant bits,
     *                   and the major version in the 16 least significant bits.
     * @param access     the class's access flags (see {@link Opcodes}). This parameter also indicates if
     *                   the class is deprecated.
     * @param name       the internal name of the class (see {@link Type#getInternalName()}).
     * @param signature  the signature of this class. May be {@literal null} if the class is not a
     *                   generic one, and does not extend or implement generic classes or interfaces.
     * @param superName  the internal of name of the super class (see {@link Type#getInternalName()}).
     *                   For interfaces, the super class is {@link Object}. May be {@literal null}, but only for the
     *                   {@link Object} class.
     * @param interfaces the internal names of the class's interfaces (see {@link
     *                   Type#getInternalName()}). May be {@literal null}.
     */
    @Override
    public void visit(int version, int access, String name, String signature, String superName, String[] interfaces) {
        super.visit(version, access, name, signature, superName, interfaces);
    }

    /**
     * Visits the source of the class.
     *
     * @param source the name of the source file from which the class was compiled. May be {@literal
     *               null}.
     * @param debug  additional debug information to compute the correspondence between source and
     *               compiled elements of the class. May be {@literal null}.
     */
    @Override
    public void visitSource(String source, String debug) {
        super.visitSource(source, debug);
    }

    /**
     * Visits the nest host class of the class. A nest is a set of classes of the same package that
     * share access to their private members. One of these classes, called the host, lists the other
     * members of the nest, which in turn should link to the host of their nest. This method must be
     * called only once and only if the visited class is a non-host member of a nest. A class is
     * implicitly its own nest, so it's invalid to call this method with the visited class name as
     * argument.
     *
     * @param nestHost the internal name of the host class of the nest.
     */
    @Override
    public void visitNestHost(String nestHost) {
        super.visitNestHost(nestHost);
    }

    /**
     * Visits the enclosing class of the class. This method must be called only if the class has an
     * enclosing class.
     *
     * @param owner      internal name of the enclosing class of the class.
     * @param name       the name of the method that contains the class, or {@literal null} if the class is
     *                   not enclosed in a method of its enclosing class.
     * @param descriptor the descriptor of the method that contains the class, or {@literal null} if
     */
    @Override
    public void visitOuterClass(String owner, String name, String descriptor) {
        super.visitOuterClass(owner, name, descriptor);
    }

    /**
     * Visits an annotation of the class.
     *
     * @param descriptor the class descriptor of the annotation class.
     * @param visible    {@literal true} if the annotation is visible at runtime.
     * @return a visitor to visit the annotation values, or {@literal null} if this visitor is not
     * interested in visiting this annotation.
     */
    @Override
    public AnnotationVisitor visitAnnotation(String descriptor, boolean visible) {
        return super.visitAnnotation(descriptor, visible);
    }

    /**
     * Visits an annotation on a type in the class signature.
     *
     * @param typeRef    a reference to the annotated type. The sort of this type reference must be
     *                   {@link TypeReference#CLASS_TYPE_PARAMETER}, {@link
     *                   TypeReference#CLASS_TYPE_PARAMETER_BOUND} or {@link TypeReference#CLASS_EXTENDS}. See
     *                   {@link TypeReference}.
     * @param typePath   the path to the annotated type argument, wildcard bound, array element type, or
     *                   static inner type within 'typeRef'. May be {@literal null} if the annotation targets
     *                   'typeRef' as a whole.
     * @param descriptor the class descriptor of the annotation class.
     * @param visible    {@literal true} if the annotation is visible at runtime.
     * @return a visitor to visit the annotation values, or {@literal null} if this visitor is not
     * interested in visiting this annotation.
     */
    @Override
    public AnnotationVisitor visitTypeAnnotation(int typeRef, TypePath typePath, String descriptor, boolean visible) {
        return super.visitTypeAnnotation(typeRef, typePath, descriptor, visible);
    }

    /**
     * Visits a non standard attribute of the class.
     *
     * @param attribute an attribute.
     */
    @Override
    public void visitAttribute(Attribute attribute) {
        super.visitAttribute(attribute);
    }

    /**
     * Visits a member of the nest. A nest is a set of classes of the same package that share access
     * to their private members. One of these classes, called the host, lists the other members of the
     * nest, which in turn should link to the host of their nest. This method must be called only if
     * the visited class is the host of a nest. A nest host is implicitly a member of its own nest, so
     * it's invalid to call this method with the visited class name as argument.
     *
     * @param nestMember the internal name of a nest member.
     */
    @Override
    public void visitNestMember(String nestMember) {
        super.visitNestMember(nestMember);
    }

    /**
     * Visits information about an inner class. This inner class is not necessarily a member of the
     * class being visited.
     *
     * @param name      the internal name of an inner class (see {@link Type#getInternalName()}).
     * @param outerName the internal name of the class to which the inner class belongs (see {@link
     *                  Type#getInternalName()}). May be {@literal null} for not member classes.
     * @param innerName the (simple) name of the inner class inside its enclosing class. May be
     *                  {@literal null} for anonymous inner classes.
     * @param access    the access flags of the inner class as originally declared in the enclosing
     */
    @Override
    public void visitInnerClass(String name, String outerName, String innerName, int access) {
        super.visitInnerClass(name, outerName, innerName, access);
    }

    /**
     * Visits a field of the class.
     *
     * @param access     the field's access flags (see {@link Opcodes}). This parameter also indicates if
     *                   the field is synthetic and/or deprecated.
     * @param name       the field's name.
     * @param descriptor the field's descriptor (see {@link Type}).
     * @param signature  the field's signature. May be {@literal null} if the field's type does not use
     *                   generic types.
     * @param value      the field's initial value. This parameter, which may be {@literal null} if the
     *                   field does not have an initial value, must be an {@link Integer}, a {@link Float}, a {@link
     *                   Long}, a {@link Double} or a {@link String} (for {@code int}, {@code float}, {@code long}
     *                   or {@code String} fields respectively). <i>This parameter is only used for static
     *                   fields</i>. Its value is ignored for non static fields, which must be initialized through
     *                   bytecode instructions in constructors or methods.
     * @return a visitor to visit field annotations and attributes, or {@literal null} if this class
     * visitor is not interested in visiting these annotations and attributes.
     */
    @Override
    public FieldVisitor visitField(int access, String name, String descriptor, String signature, Object value) {
        return super.visitField(access, name, descriptor, signature, value);
    }

    /**
     * Visits a method of the class. This method <i>must</i> return a new {@link MethodVisitor}
     * instance (or {@literal null}) each time it is called, i.e., it should not return a previously
     * returned visitor.
     *
     * @param access     the method's access flags (see {@link Opcodes}). This parameter also indicates if
     *                   the method is synthetic and/or deprecated.
     * @param name       the method's name.
     * @param descriptor the method's descriptor (see {@link Type}).
     * @param signature  the method's signature. May be {@literal null} if the method parameters,
     *                   return type and exceptions do not use generic types.
     * @param exceptions the internal names of the method's exception classes (see {@link
     *                   Type#getInternalName()}). May be {@literal null}.
     * @return an object to visit the byte code of the method, or {@literal null} if this class
     * visitor is not interested in visiting the code of this method.
     */
    @Override
    public MethodVisitor visitMethod(int access, String name, String descriptor, String signature, String[] exceptions) {
        return super.visitMethod(access, name, descriptor, signature, exceptions);
    }

    /**
     * Visits the end of the class. This method, which is the last one to be called, is used to inform
     * the visitor that all the fields and methods of the class have been visited.
     */
    @Override
    public void visitEnd() {
        super.visitEnd();
    }
}
