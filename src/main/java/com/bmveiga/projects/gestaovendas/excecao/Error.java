package com.bmveiga.projects.gestaovendas.excecao;

public class Error {

	private String messageUser;

	private String messageDev;

	public Error(String messageUser, String messageDev) {
		this.messageUser = messageUser;
		this.messageDev = messageDev;
	}

	public String getMessageUser() {
		return messageUser;
	}

	public String getMessageDev() {
		return messageDev;
	}

}
