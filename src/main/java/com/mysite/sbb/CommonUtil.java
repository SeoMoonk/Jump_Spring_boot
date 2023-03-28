package com.mysite.sbb;


import org.commonmark.node.Node;
import org.commonmark.parser.Parser;
import org.commonmark.renderer.html.HtmlRenderer;
import org.springframework.stereotype.Component;

@Component      //스프링부트에 의해 관리되는 빈(Bean, 객체)가 되었음. (빈은 템플릿에서 바로 사용 가능)
public class CommonUtil {

    public String markdown(String markdown) {

        //마크다운 텍스트를 HTML 문서로 변환하여 리턴

        Parser parser = Parser.builder().build();
        Node document = parser.parse(markdown);
        HtmlRenderer renderer = HtmlRenderer.builder().build();

        return renderer.render(document);
    }

}
