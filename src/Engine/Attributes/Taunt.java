package Engine.Attributes;

public class Taunt {
	public Taunt(String name,String key) {
		this.name = name;
		this.key = key;
		this.file = "shared/effects/taunts/"+key+"/death.effect";
	}
	//CoN
	public String name;
	public String key;
	//General
	public String file;
	public String getXAML(Taunt taunt) {
		String str = "";
		String[] path = new String[10];
		path = taunt.file.split("/");
		String dir0 = "/";
		String dir1 = "/";
		String dir2 = "/";
		String dir3 = "/";
		//making dir's
		for(int ii = 0;ii <= path.length-2;ii++) {
			dir0 += path[ii]+"/";
		}
		for(int ii = 0;ii <= path.length-3;ii++) {
			dir1 = path[ii]+"/";
		}
		for(int ii = 0;ii <= path.length-4;ii++) {
			dir2 += path[ii]+"/";
		}
		for(int ii = 0;ii <= path.length-5;ii++) {
			dir3 += path[ii]+"/";
		}
		str +=	"\n	<copyfile name='"+file+"' source='"+taunt.file+"' overwrite='yes' fromresource='true' />\n" +
				"	<editfile name='"+file+"'>\n" +
				"		<findall><![CDATA[sample=']]></findall><replace><![CDATA[sample='"+dir0+"]]></replace><find position='start'/>\n" +
				"		<findall><![CDATA[material=']]></findall><replace><![CDATA[material='"+dir0+"]]></replace><find position='start'/>\n" +
				"		<findall><![CDATA[model=']]></findall><replace><![CDATA[model='"+dir0+"]]></replace><find position='start'/>\n" +
				"		<findall><![CDATA[icon=']]></findall><replace><![CDATA[icon='"+dir0+"]]></replace><find position='start'/>\n" +
				"		<findall><![CDATA[portrait=']]></findall><replace><![CDATA[portrait='"+dir0+"]]></replace><find position='start'/>\n" +
				"		<findall><![CDATA[effect=']]></findall><replace><![CDATA[effect='"+dir0+"]]></replace><find position='start'/>\n" +
				"		<findall><![CDATA[Linear ]]></findall><replace><![CDATA[Linear "+dir0+"]]></replace><find position='start'/>\n" +
				"		<findall><![CDATA[Effect ]]></findall><replace><![CDATA[Effect "+dir0+"]]></replace><find position='start'/>\n" +
				//models
				"		<findall><![CDATA[file=']]></findall><replace><![CDATA[file='"+dir0+"]]></replace><find position='start'/>\n" +
				"		<findall><![CDATA[low=']]></findall><replace><![CDATA[low='"+dir0+"]]></replace><find position='start'/>\n" +
				"		<findall><![CDATA[med=']]></findall><replace><![CDATA[med='"+dir0+"]]></replace><find position='start'/>\n" +
				"		<findall><![CDATA[high=']]></findall><replace><![CDATA[high='"+dir0+"]]></replace><find position='start'/>\n" +
				"		<findall><![CDATA[clip=']]></findall><replace><![CDATA[clip='"+dir0+"]]></replace><find position='start'/>\n" +
				//remove failure's
				"		<findall><![CDATA["+dir0+"/heroes]]></findall><replace><![CDATA[/heroes]]></replace><find position='start'/>\n" +
				"		<findall><![CDATA["+dir0+"/shared]]></findall><replace><![CDATA[/shared]]></replace><find position='start'/>\n" +
				"		<findall><![CDATA["+dir0+"/ui]]></findall><replace><![CDATA[/ui]]></replace><find position='start'/>\n" +
				//"../"
				"		<findall><![CDATA["+dir0+"../../../]]></findall><replace><![CDATA["+dir3+"]]></replace><find position='start'/>\n" +
				"		<findall><![CDATA["+dir0+"../../]]></findall><replace><![CDATA["+dir2+"]]></replace><find position='start'/>\n" +
				"		<findall><![CDATA["+dir0+"../]]></findall><replace><![CDATA["+dir1+"]]></replace><find position='start'/>\n" +
				"	</editfile>\n";
		return str;
	}
}
