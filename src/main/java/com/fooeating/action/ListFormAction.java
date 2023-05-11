package com.fooeating.action;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.fooeating.commons.Action;
import com.fooeating.commons.ActionForward;
import com.fooeating.db.PublicDAO;
import com.fooeating.db.RestaurantDTO;

public class ListFormAction implements Action {

	@Override
	public ActionForward execute(HttpServletRequest request, 
			HttpServletResponse response) throws Exception {
		System.out.println(" M : listFormAction_execute() 호출");
		
		// 한글 처리
		request.setCharacterEncoding("UTF-8");
		
		// 검색어 search(파라미터) 정보를 저장
		String search = request.getParameter("search");
		System.out.println("search : " + search);
		
		
		PublicDAO dao = new PublicDAO();
		
		int count1 = 0;

		if(search != null) { // 검색어 있을 때
			count1 = dao.getListCount(search.trim());
		} else {// 검색어 없을 때
			count1 = dao.getListCount();
		}
		
		System.out.println("M : 총 글의 수 : " + count1);
		
		
		
		
		List<RestaurantDTO> listForm = dao.getlistForm();
		System.out.println("회원 수 : " + listForm.size());
		
		request.setAttribute("listForm", listForm);
		
		ActionForward forward = new ActionForward();
		
		// 페이징 처리 -----------------
		int count = dao.getListCount(); 			// 전체 가게 수
		System.out.println("가게수 : " + count);
		
		int pageSize = 10;							// 한 페이지에 출력할 가게 수
		
		
		String pno = request.getParameter("pno");
		if(pno == null) { pno = "1"; }				// 현재 페이지 정보 체크
		
		int currentPage = Integer.parseInt(pno);
		int startRow = (currentPage - 1) * pageSize + 1;	// 시작행 번호 계산
		int endRow = currentPage * pageSize;				// 끝행 번호 계산
		
		
		// ---------------------------------------------------------------------
		List restList = null;
		
		if(search != null) {
			restList = dao.getListInfo(startRow, pageSize, search.trim());
		} else {
			restList = dao.getListInfo(startRow, pageSize);
		}
		
		
		// 페이징 처리 -----------------
		
//		List<RestaurantDTO> restList = dao.getListInfo(startRow, pageSize);
		
		request.setAttribute("restList", restList);
		request.setAttribute("count", count);
		request.setAttribute("currentPage", currentPage);
		request.setAttribute("pageSize", pageSize);
		request.setAttribute("pno", pno);
		// 연결된 view에 출력
		forward.setPath("./list/gallery.jsp");
		forward.setRedirect(false);
		
		return forward;
	}
}
