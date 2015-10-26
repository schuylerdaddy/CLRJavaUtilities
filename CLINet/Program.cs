using System;
using System.Collections.Generic;
using System.Collections;
using System.Threading.Tasks;
using System.Net.Http;
using System.Net.Http.Headers;

namespace CLIWebService
{
	class MainClass
	{
		static string[] CLIargs;
		static Dictionary<String,String> arguments = new Dictionary<String,String>();
		static Dictionary<String,String> headers = new Dictionary<String,String>(); 
		
		public static void Main (string[] args)
		{
			try{
				CLIargs = args;
				RunAsync().Wait();
			}

			catch(Exception ex){

				Console.WriteLine("Error occured in dot net!");
				Console.Error.WriteLine(ex.StackTrace);
			}
		}

		static async Task RunAsync()
		{
			String command = arguments ["command"];
			Console.WriteLine ("Loading args");
			LoadClient ();
			Console.WriteLine ("Finished args");
			HttpResponseMessage response = null;
			Console.WriteLine ("Processing Command '"+command + "'");
			switch(command.ToLower()){

			case "get":
				HttpClient client = new HttpClient ();
				Console.WriteLine ("Attempting uri: " + arguments ["uri"]);
				client.BaseAddress = new Uri (arguments ["uri"]);


				client.DefaultRequestHeaders.Accept.Add(
					new MediaTypeWithQualityHeaderValue("text/plain"));
				response = await client.GetAsync ("");
				if (response.IsSuccessStatusCode) {
					Console.WriteLine(response.ToString ());
				}
				else {
					ProcessResponseCode ((int)response.StatusCode);
				}
				break;
			case "post":
				HttpClient postClient = new HttpClient ();
				postClient.BaseAddress = new Uri (arguments ["uri"]);
				var content = new FormUrlEncodedContent (new[] {
					new KeyValuePair<string, string> ("", arguments ["body"])
				});
				response = await postClient.PostAsync (arguments ["uri"], content);
				if (response.IsSuccessStatusCode) {
					Console.WriteLine (response.ToString ());
				} else {
					ProcessResponseCode ((int)response.StatusCode);
				}
				break;
			case "wsdl":
				WSDLManager wsdl = new WSDLManager ();
				wsdl.runService ("ChannelAdvisor.Admin",arguments);//arguments["wsdl"],arguments);
				break;
			default:
				throw new Exception ("Invalid command '"+command+"'");
			}
		}

		/// <summary>
		/// sets command to be executed, loads CLI arguments into dictionaries for fast referencing 
		/// </summary>
		static void LoadClient(){
			if (CLIargs.Length == 0)
				throw new Exception ("~*~MagicException:Domo is ready but encountered an error in the network:No command argument passed to .NET CLI");

			for (int idx = 1; idx < CLIargs.Length; ++idx) {
				string[] keyValue = CLIargs [idx].Split (':');
				string key = keyValue [0];
				string value = keyValue[1];
				for (int idx2 = 2; idx2 < keyValue.Length; ++idx2)
					value += ":" + keyValue [idx2];
				if (CLIargs [idx].StartsWith ("\"")) {
					headers [key] = value;
				} else {
					arguments [key] = value;
				}
			}
		}

		/// <summary>
		/// Determines Error and throws it with message
		/// </summary>
		/// <param name="code">Response Code Number</param>
		static void ProcessResponseCode(int code){
			if (code == 401) {
				throw new Exception ("~*~HttpException:Domo is ready but authentication failed with the credentials provided:Receive 401 error in .NET CLI");
			} else if (code % 400 == 4) {
				throw new Exception ("~*~HttpException:Domo is ready but authentication failed with the credentials provided:Receive 401 error in .NET CLI");
			}
		}	
	}
}
