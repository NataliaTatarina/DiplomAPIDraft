package diplom_api.pojo;

//import com.fasterxml.jackson.annotation.JsonInclude;

import com.fasterxml.jackson.annotation.JsonInclude;

public class UserRegister {
   // @JsonInclude(JsonInclude.Include.NON_NULL)
    private String email;
  //  @JsonInclude(JsonInclude.Include.NON_NULL)
    private String password;
   // @JsonInclude(JsonInclude.Include.NON_NULL)
    private String name;

    public UserRegister() {

    }

    public UserRegister(String email, String password, String name) {
        this.email = email;
        this.password = password;
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
