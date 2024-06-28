package com.ssafy.firskorea.util;

import java.util.ArrayList;
import java.util.List;

import com.ssafy.firskorea.board.dto.CommentDto;

public class CommentStratify {
	
	private static List<CommentDto> list;
	private static int[] commentIds;
	private static List<List<Integer>> graph;
	private static List<CommentDto> stratifiedList;
	private static boolean[] isAdded;
	
	public static void stratify(List<CommentDto> l) {
		list = l;
		
		// 댓글 번호 인덱스로 변환
		convertCommentIdToIdx();
		
		// 댓글 그래프 생성하기 with 인접 리스트
		makeGraph();
		
		// 정렬된 댓글 리스트 생성하기
		stratifiedList = new ArrayList<>();
		isAdded = new boolean[commentIds.length];
		for (int i = 0; i < commentIds.length; i++) {
			if (!isAdded[i]) {
				isAdded[i] = true;
				makeStratifiedList(i, 0);
			}
		}
		
		l.clear();
		l.addAll(stratifiedList);
	}
	
	private static void convertCommentIdToIdx() {
		commentIds = new int[list.size()];
		CommentDto comment = null;
		for (int i = 0; i < list.size(); i++) {
			comment = list.get(i);
			commentIds[i] = comment.getId();
			
			// 삭제한 댓글인 경우 내용 처리
			if (!comment.isExisted()) {
				comment.setContent("This comment has already been deleted.");
			}
			
			// 탈퇴한 회원의 댓글인 경우 작성자 처리
			if (!comment.isExistedOfMember()) {
				comment.setMemberId("(withdrawn member)");
			}
		}
	}
	
	private static void makeGraph() {
		graph = new ArrayList<>();
		for (int i = 0; i < commentIds.length; i++) {
			graph.add(new ArrayList<>());
		}
		
		int parentCommentId;
		int parentIdx, idx;
		for (CommentDto comment : list) {
			parentCommentId = comment.getParentCommentId();
			
			if (parentCommentId > 0) {
				parentIdx = getIdx(parentCommentId);
				idx = getIdx(comment.getId());
				
				graph.get(parentIdx).add(idx);
			}
		}
	}
	
	private static int getIdx(int memoNo) {
		for (int i = 0; i < commentIds.length; i++) {
			if (commentIds[i] == memoNo) {
				return i;
			}
		}
		
		return -1;
	}
	
	private static void makeStratifiedList(int idx, int depth) {
		list.get(idx).setDepth(depth);
		stratifiedList.add(list.get(idx));
		
		for (int childIdx : graph.get(idx)) {
			if (!isAdded[childIdx]) {
				isAdded[childIdx] = true;
				makeStratifiedList(childIdx, depth + 1);
			}
		}
	}

}
