class SymbolVisitor extends GJDepthFirst<String, Void>{

    // SymbolNode VarTable = new SymbolNode();
    // SymbolNode ClassTable = new SymbolNode();
    // SymbolNode MethodTable = new SymbolNode();
    SymbolTable table = new SymbolTable();

    // public SymbolNode getSymbolTable(int flag) {
    //     switch (flag) {
    //         case 0:
    //             return this.VarTable;
    //         case 1:
    //             return this.ClassTable;
    //         case 2:
    //             return this.MethodTable;
    //         default:
    //             return null;
    //     }
    // }
    /**
     * f0 -> "class"
     * f1 -> Identifier()
     * f2 -> "{"
     * f3 -> "public"
     * f4 -> "static"
     * f5 -> "void"
     * f6 -> "main"
     * f7 -> "("
     * f8 -> "String"
     * f9 -> "["
     * f10 -> "]"
     * f11 -> Identifier()
     * f12 -> ")"
     * f13 -> "{"
     * f14 -> ( VarDeclaration() )*
     * f15 -> ( Statement() )*
     * f16 -> "}"
     * f17 -> "}"
     */
    @Override
    public String visit(MainClass n, Void argu) throws Exception {
        // this.VarTable.enter();
        // this.MethodTable.enter();
        this.table.enter();

        String classname = n.f1.accept(this, null);
        Entry e = new Entry();
        e.setType("Class");
        e.setName(classname);
        if (!this.table.insert(e, 0))
            throw new Exception("Duplicate class: " + classname);

        System.out.println("Class: " + classname);

        String name = n.f11.accept(this, null);

        Entry e2 = new Entry();
        e2.setType("String[]");
        e2.setName(name);

        if (!this.table.insert(e2, 3))
            throw new Exception("Identifier already declared!");

        this.visit(n.f14, argu);
        this.visit(n.f15, argu);

        // this.VarTable.exit();
        // this.MethodTable.exit();
        this.table.exit();

        return null;
    }

    /**
     * f0 -> "class"
     * f1 -> Identifier()
     * f2 -> "{"
     * f3 -> ( VarDeclaration() )*
     * f4 -> ( MethodDeclaration() )*
     * f5 -> "}"
     */
    @Override
    public String visit(ClassDeclaration n, Void argu) throws Exception {

        // this.VarTable.enter();
        // this.MethodTable.enter();
        this.table.enter();

        String classname = n.f1.accept(this, null);

        Entry e = new Entry();
        e.setType("Class");
        e.setName(classname);
        if (!this.table.insert(e, 0))
            throw new Exception("Class identifier already declared!");

        System.out.println("Class: " + classname);

        this.visit(n.f3, argu);
        this.visit(n.f4, argu);

        this.table.exit();
        // this.VarTable.exit();
        // this.MethodTable.exit();

        System.out.println();

        return null;
    }

    /**
     * f0 -> "class"
     * f1 -> Identifier()
     * f2 -> "extends"
     * f3 -> Identifier()
     * f4 -> "{"
     * f5 -> ( VarDeclaration() )*
     * f6 -> ( MethodDeclaration() )*
     * f7 -> "}"
     */
    @Override
    public String visit(ClassExtendsDeclaration n, Void argu) throws Exception {

        // this.VarTable.enter();
        // this.MethodTable.enter();
        this.table.enter();


        String superName = n.f3.accept(this, null);
        if (this.table.lookup(superName, 0) == null)
            throw new Exception("Cannot find symbol: " + superName);


        String classname = n.f1.accept(this, null);

        Entry e = new Entry();
        e.setType("Class");
        e.setName(classname);
        if (!this.table.insert(e, superName))
            throw new Exception("Class identifier already declared!");

        System.out.println("Class: " + classname);

        this.visit(n.f5, argu);
        this.visit(n.f6, argu);

        if (!this.table.checkOverload())
            throw new Exception("Overloading!");

        // this.VarTable.exit();
        // this.MethodTable.exit();
        this.table.exit();


        System.out.println();

        return null;
    }

    /**
     * f0 -> "public"
     * f1 -> Type()
     * f2 -> Identifier()
     * f3 -> "("
     * f4 -> ( FormalParameterList() )?
     * f5 -> ")"
     * f6 -> "{"
     * f7 -> ( VarDeclaration() )*
     * f8 -> ( Statement() )*
     * f9 -> "return"
     * f10 -> Expression()
     * f11 -> ";"
     * f12 -> "}"
     */
    @Override
    public String visit(MethodDeclaration n, Void argu) throws Exception {
        // this.VarTable.enter();

        String myType = n.f1.accept(this, null);
        String myName = n.f2.accept(this, null);

        Entry e = new Entry();
        e.setType(myType);
        e.setName(myName);
        if (!this.table.insert(e, 1))
            throw new Exception("Method already declared!");

        this.visit(n.f4, argu);
        this.visit(n.f7, argu);
        this.visit(n.f8, argu);

        // this.VarTable.exit();

        return null;
    }

    /**
     * f0 -> Identifier()
     * f1 -> "="
     * f2 -> Expression()
     * f3 -> ";"
     */
    @Override
    public String visit(AssignmentStatement n, Void argu) throws Exception {
        System.out.println(n.f0.accept(this, argu) + " " + n.f2.accept(this, argu));
        return null;
    }

    /**
     * f0 -> Identifier()
     * f1 -> "["
     * f2 -> Expression()
     * f3 -> "]"
     * f4 -> "="
     * f5 -> Expression()
     * f6 -> ";"
     */
    @Override
    public String visit(ArrayAssignmentStatement n, Void argu) throws Exception {

        return null;
    }

    /**
     * f0 -> "if"
     * f1 -> "("
     * f2 -> Expression()
     * f3 -> ")"
     * f4 -> Statement()
     * f5 -> "else"
     * f6 -> Statement()
     */
    @Override
    public String visit(IfStatement n, Void argu) throws Exception {

        return null;
    }

    /**
     * f0 -> "while"
     * f1 -> "("
     * f2 -> Expression()
     * f3 -> ")"
     * f4 -> Statement()
     */
    @Override
    public String visit(WhileStatement n, Void argu) throws Exception {

        return null;
    }

    /**
     * f0 -> "System.out.println"
     * f1 -> "("
     * f2 -> Expression()
     * f3 -> ")"
     * f4 -> ";"
     */
    @Override
    public String visit(PrintStatement n, Void argu) throws Exception {

        return null;
    }


    /**
     * f0 -> FormalParameter()
     * f1 -> FormalParameterTail()
     */
    @Override
    public String visit(FormalParameterList n, Void argu) throws Exception {

        this.visit(n.f0, argu);

        if (n.f1 != null)
            this.visit(n.f1, argu);

        return null;
    }

    /**
     * f0 -> FormalParameter()
     * f1 -> FormalParameterTail()
     */
    public String visit(FormalParameterTerm n, Void argu) throws Exception {
        return n.f1.accept(this, argu);
    }

    /**
     * f0 -> ","
     * f1 -> FormalParameter()
     */
    @Override
    public String visit(FormalParameterTail n, Void argu) throws Exception {

        this.visit(n.f0, argu);

        return null;
    }

    /**
     * f0 -> Type()
     * f1 -> Identifier()
     */
    @Override
    public String visit(FormalParameter n, Void argu) throws Exception{
        String type = n.f0.accept(this, null);
        String name = n.f1.accept(this, null);

        Entry e = new Entry();
        e.setName(name);
        e.setType(type);
        if (!this.table.insert(e, 2))
            throw new Exception("Parameter already declared!");

        return null;
    }

    @Override
    public String visit(VarDeclaration n, Void argu) throws Exception {

        String myType = n.f0.accept(this, null);
        String myName = n.f1.accept(this, null);

        Entry e = new Entry();
        e.setName(myName);
        e.setType(myType);
        if (!this.table.insert(e, 3))
          throw new Exception("Identifier already declared!");

        return null;
    }

    @Override
    public String visit(ArrayType n, Void argu) {
        return "int[]";
    }

    public String visit(BooleanType n, Void argu) {
        return "boolean";
    }

    public String visit(IntegerType n, Void argu) {
        return "int";
    }

    @Override
    public String visit(Identifier n, Void argu) {
            return n.f0.toString();
    }
}
