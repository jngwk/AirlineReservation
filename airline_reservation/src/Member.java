public class Member implements Queryable{
    private String id; // primary key
    private String pwd;
    private String firstName;
    private String lastName;
    private String passport;
    private String sex;
    
    public Member(String id, String pwd, String firstName, String lastName, String passport, String sex) {
        this.id = id;
        this.pwd = pwd;
        this.firstName = firstName;
        this.lastName = lastName;
        this.passport = passport;
        this.sex = sex;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPwd() {
        return pwd;
    }

    public void setPwd(String pwd) {
        this.pwd = pwd;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getPassport() {
        return passport;
    }

    public void setPassport(String passport) {
        this.passport = passport;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    } 
    
    public String toString(){
        return String.format("%s\t| %s, %s\t| %s\n", id, lastName, firstName, sex);
    }
}
