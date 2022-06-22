package org.apache.ws.axis2;

import java.rmi.RemoteException;

public class WSClient {
	
	public static void main(String[] args) throws RemoteException  {
        //1) Create the stub object
		WSAxis2ServicesStub stub = new WSAxis2ServicesStub();

        //2) Create the request
		WSAxis2ServicesStub.IssueTicket request = new WSAxis2ServicesStub.IssueTicket();

        //3) Set the parameters
        request.setI(100);

        //4) Invoke the service if any
        //if response expected
        WSAxis2ServicesStub.IssueTicketResponse response = stub.issueTicket(request);
        System.out.println(response.get_return());
        //if not
        //stub.issueTicket(request);
	}

}
