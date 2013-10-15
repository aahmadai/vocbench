package org.fao.aoscs.client.module.comment.service;

import java.util.ArrayList;

import org.fao.aoscs.domain.UserComments;

import com.google.gwt.user.client.rpc.AsyncCallback;

public interface CommentServiceAsync<T> {
	public void getComments(String module, AsyncCallback<ArrayList<UserComments>> callback);
	public void sendComment(UserComments uc, AsyncCallback<String> callback);
}
