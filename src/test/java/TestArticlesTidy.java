import com.huimang.ganhuo.entity.ArticleEntity;
import com.huimang.ganhuo.tidy.ArticlesTidy;

import java.io.File;
import java.util.List;

/**
 * @author ronnie
 * @copyright 2016 (c) huimang.com
 * @since 2016/7/17 11:49
 */
public class TestArticlesTidy {
    public static void main(String[] args) {
        String path = System.getProperty("user.dir") + "/src/main/resources/articles.html";
        List<ArticleEntity> list = ArticlesTidy.tidy(new File(path));
        System.out.print(list.size());
    }
}
