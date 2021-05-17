import java.util.ArrayList;

class SymbolTable {
    ArrayList <ClassEntry> classTable = new ArrayList <ClassEntry> ();
    int classNum = -1;

    public void enter() {
        this.classNum += 1;
    }

    public void exit() {
        if (this.classNum > 0)
            this.classNum -= 1;
    }

    private boolean insertClass(Entry e) {

        for (int i = 0; i < this.classTable.size(); ++i)
            if (this.classTable.get(i).getName() == e.getName())
                return false;

        ClassEntry cEntry = new ClassEntry ();
        cEntry.setType(e.getType());
        cEntry.setName(e.getName());
        this.classTable.add(cEntry);
        return true;

    }

    private boolean insertMethod(Entry e) {

        int classNum = this.classTable.size() -1;

        Table t = this.classTable.get(classNum).getTable();

        ArrayList <MethodEntry> methods = t.getMethods();
        for (int i = 0; i < methods.size(); ++i)
            if (methods.get(i).getMethod().getName() == e.getName())
                return false;


        MethodEntry mEntry = new MethodEntry();
        Entry e2 = mEntry.getMethod();
        e2.setType(e.getType());
        e2.setName(e.getName());

        methods.add(mEntry);
        return true;

    }

    private boolean insertVarParam(Entry e) {
      int classNum = this.classTable.size() -1;

      Table t = this.classTable.get(classNum).getTable();

      ArrayList <MethodEntry> methods = t.getMethods();
      int methodNum = methods.size() -1;
      MethodEntry method = methods.get(methodNum);

      ArrayList <Entry> param = method.getParam();

      for (int i = 0; i < param.size(); ++i)
          if (param.get(i).getName() == e.getName())
              return false;

      param.add(e);

      return true;

    }


    private boolean insertVar(Entry e) {
        int classNum = this.classTable.size() -1;

        Table t = this.classTable.get(classNum).getTable();

        ArrayList <MethodEntry> methods = t.getMethods();
        if (methods.size() == 0) {
            ArrayList <Entry> varDecl = t.getTable();
            for (int i = 0; i < varDecl.size(); ++i)
                if (varDecl.get(i).getName() == e.getName())
                    return false;

            varDecl.add(e);
            return true;
        }


        int methodNum = methods.size() -1;
        MethodEntry method = methods.get(methodNum);

        ArrayList <Entry> param = method.getParam();

        for (int i = 0; i < param.size(); ++i)
            if (param.get(i).getName() == e.getName())
                return false;

        ArrayList <Entry> table = method.getTable();

        for (int i = 0; i < table.size(); ++i)
            if (table.get(i).getName() == e.getName())
                return false;

        table.add(e);

        return true;
    }

    public boolean insert(Entry e, String superName) {
        ClassEntry cEntry = new ClassEntry ();
        for (int i = 0; i < this.classTable.size(); ++i) {
            if (this.classTable.get(i).getName() == e.getName())
                return false;
            if (this.classTable.get(i).getName() == superName)
                cEntry.setInhClass(this.classTable.get(i));
        }

        cEntry.setType(e.getType());
        cEntry.setName(e.getName());
        this.classTable.add(cEntry);
        return true;

    }

    public boolean insert(Entry e, int flag) {
        switch (flag) {
            /* Class */
            case 0:
                return insertClass(e);
            /* Method */
            case 1:
                // System.out.println("Method");
                return insertMethod(e);
            /* Param */
            case 2:
                // System.out.println("Param");
                return insertVarParam(e);
            /* Var */
            case 3:
                return insertVar(e);
            default:
                return false;
        }
    }

    private boolean paramCompare(ArrayList <Entry> p1, ArrayList <Entry> p2) {
        if (p1.size() != p2.size())
            return false;

        for (int i = 0; i < p1.size(); ++i) {
            Entry e1 = p1.get(i);
            Entry e2 = p2.get(i);
            if (e1.getType() != e2.getType())
                return false;
        }
        return true;
    }

    private boolean methodCompare(MethodEntry m1, MethodEntry m2) {
        Entry method1 = m1.getMethod();
        Entry method2 = m2.getMethod();
        if (method1.getName() == method2.getName()) {
            // System.out.println(method1.getName());
            // System.out.println(method2.getName());

            if (method1.getType() == method2.getType())
                return paramCompare(m1.getParam(), m2.getParam());
            else
                return false;
        }

        return true;
    }

    private boolean checkMethods(ArrayList <MethodEntry> subMethods,
                                  ArrayList <MethodEntry> superMethods) {

        for (int i = 0; i < subMethods.size(); ++i) {
            MethodEntry subMethod = subMethods.get(i);
            for (int j = 0; j < superMethods.size(); ++j) {
                MethodEntry superMethod = superMethods.get(j);
                if (!methodCompare(subMethod, superMethod))
                    return false;
            }
        }
        return true;

    }

    public boolean checkOverload() {
        int subClassNum = this.classTable.size() -1;
        ClassEntry subClass = this.classTable.get(subClassNum);
        ArrayList <MethodEntry> subMethods = subClass.getTable().getMethods();

        ClassEntry superClass = subClass.getInhClass();
        while (superClass != null) {
            ArrayList <MethodEntry> superMethods = superClass.getTable().getMethods();
            if (!checkMethods(subMethods, superMethods))
                return false;
            superClass = superClass.getInhClass();
        }

        return true;
    }

    private String findSuper(String name) {
        for (int i = 0; i < this.classTable.size(); ++i)
            if (this.classTable.get(i).getName() == name)
                return name;
        return null;
    }

    public String lookup(String name, int flag) {
      switch (flag) {
          /* Class */
          case 0:
              return findSuper(name);
          default:
              return null;
      }
    }
}
