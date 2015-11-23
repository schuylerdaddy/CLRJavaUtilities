using System;
using System.Collections.Generic;
using CLIWebService.api.channeladvisor.com;

namespace CLIWebService
{
	public class CAAdmin: IDomoService
	{
		public void EnforceArguments (string command, Dictionary<String,String> args){
			
		}

		public void Execute (string command){
			AdminService asv = new AdminService ();
			asv.GetAuthorizationList ("120082");
		}
	}
}

