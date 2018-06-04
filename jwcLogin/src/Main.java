import com.h3hz.thl.util.JwcUtil;

public class Main {
    static private void setProxy(){
        System.setProperty("http.proxySet", "true");
        System.setProperty("http.proxyHost", "127.0.0.1");
        System.setProperty("http.proxyPort", "12345");
    }
    public static void main(String[] args){
        setProxy();
        String username = "用户名啊";
        String password = "密码啊";
        String str = JwcUtil.loginPost(username, password, JwcUtil.entryPage());
        System.out.println(str);
        if (str.contains("认证失败"))
            System.out.println("认证失败");
        if (str.contains("密码不正确"))
            System.out.println("密码错误");
        if (str.contains("退出"))
            System.out.println("登录成功");

    }
}
