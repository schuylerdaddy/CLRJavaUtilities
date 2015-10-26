using System;
using System.Collections.Generic;

namespace CLIWebService
{
	public class WSDLManager
	{
		public void runService(string wsdlKey, Dictionary<String,String> args){
			switch (wsdlKey) {
			case "ChannelAdvisor.Admin":
				//AdminService admin = new AdminService ("ac93793a-4b71-44d4-ad4d-d302c39c238c","Domo123!");
				APICredentials creds = new APICredentials () {
					DeveloperKey = "ac93793a-4b71-44d4-ad4d-d302c39c238c",
					Password = "Domo123!"
				};
				//AdminService admin = AdminService.getInstance ("ac93793a-4b71-44d4-ad4d-d302c39c238c", "Domo123!");
				AdminService admin = AdminService.getInstance (args["developerKey"], "developerPassword");
				admin.APICredentialsValue = creds;
				var result = admin.GetAuthorizationList(args["localId"]).ResultData;

				foreach (AuthorizationResponse resp in result) {
					Console.WriteLine (resp.AccountID + ":" + resp.LocalID);
				}
				break;
			}
			throw new Exception ("MagicException:Domo encountered error running service:Unable to map SOAP wsdl");
		}
	}
}

