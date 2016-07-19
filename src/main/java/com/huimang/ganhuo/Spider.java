package com.huimang.ganhuo;

import com.huimang.ganhuo.tidy.ArticlesTidy;
import com.huimang.ganhuo.tidy.NewsTidy;
import com.huimang.ganhuo.tidy.PresentationsTidy;

import java.sql.SQLException;

/**
 * @author ronnie
 * @copyright 2016 (c) huimang.com
 * @since 2016/7/17 13:38
 */
public class Spider {
    public static void main(String[] args) throws SQLException {
        int page = 1;
        if (args.length > 0) {
            try {
                page = Integer.parseInt(args[0]);
            } catch (Exception ex) {
                page = 1;
            }
        }
        System.out.println("page:" + page);

        NewsTidy newsTidy = new NewsTidy();
        ArticlesTidy artiesTidy = new ArticlesTidy();
        PresentationsTidy presesTidy = new PresentationsTidy();
        for(int i = 1; i <= page; i++) {
            System.out.println("generate page " + i);
            newsTidy.loadLists(i);
            artiesTidy.loadLists(i);
            presesTidy.loadLists(i);
        }
    }
}
