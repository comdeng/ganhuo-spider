import com.huimang.ganhuo.tidy.NewsTidy;

import java.sql.SQLException;

/**
 * @author ronnie
 * @copyright 2016 (c) huimang.com
 * @since 2016/7/17 11:49
 */
public class TestNewsTidy {
    public static void main(String[] args) {
        try {
            NewsTidy.loadLists(3);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
