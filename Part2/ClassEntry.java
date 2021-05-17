class ClassEntry extends Entry {
    ClassEntry inheritsFrom = null;
    Table table = new Table ();

    public Table getTable() {
        return this.table;
    }

    public ClassEntry getInhClass() {
        return this.inheritsFrom;
    }

    public void setInhClass(ClassEntry e) {
        this.inheritsFrom = e;
    }
}
