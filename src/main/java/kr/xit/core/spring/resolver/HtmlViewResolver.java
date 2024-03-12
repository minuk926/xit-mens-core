package kr.xit.core.spring.resolver;

import java.util.Locale;
import java.util.Objects;
import org.springframework.web.servlet.View;
import org.springframework.web.servlet.view.AbstractUrlBasedView;
import org.springframework.web.servlet.view.InternalResourceViewResolver;
import org.springframework.web.servlet.view.JstlView;

/**
 * <pre>
 * description : 리소스 요청에 "html/"가 존재하면 HtmlViewResolver가 처리하도록 한다. 존재하지 않으면 null을 반환
 * packageName : kr.xit.core.spring.resolver
 * fileName    : HtmlViewResolver
 * author      : julim
 * date        : 2023-10-04
 * ======================================================================
 * 변경일         변경자        변경 내용
 * ----------------------------------------------------------------------
 * 2023-10-04    julim       최초 생성
 *
 * </pre>
 */
public class HtmlViewResolver extends InternalResourceViewResolver {
	@Override
	protected View loadView(String viewName, Locale locale) throws Exception {
		AbstractUrlBasedView view = buildView(viewName);
		View viewObj = (View) getApplicationContext().getAutowireCapableBeanFactory().initializeBean(view, viewName);
		if (viewObj instanceof JstlView jv) {
			if (!Objects.requireNonNull(jv.getBeanName()).contains(".html")) {
				return null;
			}
		}
		return viewObj;
	}

}
