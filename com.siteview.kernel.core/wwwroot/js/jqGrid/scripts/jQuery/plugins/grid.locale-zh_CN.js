;(function($){
/**
 * jqGrid English Translation
 * Tony Tomov tony@trirand.com
 * http://trirand.com/blog/ 
 * Dual licensed under the MIT and GPL licenses:
 * http://www.opensource.org/licenses/mit-license.php
 * http://www.gnu.org/licenses/gpl.html
**/
$.jgrid = {
	defaults : {
		recordtext: "��ʾ {0} - {1} ����¼, �� {2} ��",
		emptyrecords: "û���ҵ���¼",
		loadtext: "������...",
		pgtext : "�� {0} ҳ �� {1} ҳ"
	},
	search : {
		caption: "����...",
		Find: "ȷ��",
		Reset: "����",
		odata : ['=', '<>', '<', '<=','>','>=', '��XX��ʼ','����XX��ʼ','is in','is not in','��XX��β','����XX��β','����','������'],
		groupOps: [	{ op: "����", text: "all" },	{ op: "����",  text: "any" }	],
		matchText: " ƥ��",
		rulesText: " ����"
	},
	edit : {
		addCaption: "��Ӽ�¼",
		editCaption: "�༭��¼",
		bSubmit: "�ύ",
		bCancel: "ȡ��",
		bClose: "�ر�",
		saveData: "�����Ѿ��޸�,Ҫ�����޸���?",
		bYes : "��",
		bNo : "��",
		bExit : "ȡ��",
		msg: {
			required:"������",
			number:"����������",
			minValue:"������ڻ���� ",
			maxValue:"����С�ڻ���� ",
			email: "e-mail��ʽ����ȷ",
			integer: "����������",
			date: "���ڸ�ʽ����",
			url: "URL ��ʽ���� ('http://' ���� 'https://' ��ͷ)"
		}
	},
	view : {
		caption: "��ʾ",
		bClose: "�ر�"
	},
	del : {
		caption: "ɾ��",
		msg: "ɾ��ѡ����?",
		bSubmit: "ɾ��",
		bCancel: "ȡ��"
	},
	nav : {
		edittext: "",
		edittitle: "�༭ѡ����",
		addtext:"",
		addtitle: "�������",
		deltext: "",
		deltitle: "ɾ��ѡ����",
		searchtext: "",
		searchtitle: "����",
		refreshtext: "",
		refreshtitle: "ˢ��",
		alertcap: "����",
		alerttext: "��ѡ����",
		viewtext: "",
		viewtitle: "�鿴ѡ������"
	},
	col : {
		caption: "��ʾ/������",
		bSubmit: "�ύ",
		bCancel: "ȡ��"
	},
	errors : {
		errcap : "����",
		nourl : "û������url",
		norecords: "û�м�¼",
		model : "�������ݳ���"
	},
	formatter : {
		integer : {thousandsSeparator: " ", defaultValue: '0'},
		number : {decimalSeparator:".", thousandsSeparator: " ", decimalPlaces: 2, defaultValue: '0.00'},
		currency : {decimalSeparator:".", thousandsSeparator: " ", decimalPlaces: 2, prefix: "", suffix:"", defaultValue: '0.00'},
		date : {
			dayNames:   [
				"��", "һ", "��", "��", "��", "��", "��",
				"������", "����һ", "���ڶ�", "������", "������", "������", "������",
			],
			monthNames: [
				"1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "11", "12",
				"һ��", "����", "����", "����", "����", "����", "����", "����", "����", "ʮ��", "ʮһ��", "ʮ����"
			],
			AmPm : ["am","pm","AM","PM"],
			S: function (j) {return j < 11 || j > 13 ? ['st', 'nd', 'rd', 'th'][Math.min((j - 1) % 10, 3)] : 'th'},
			srcformat: 'Y-m-d',
			newformat: 'd/m/Y',
			masks : {
				ISO8601Long:"Y-m-d H:i:s",
				ISO8601Short:"Y-m-d",
				ShortDate: "n/j/Y",
				LongDate: "l, F d, Y",
				FullDateTime: "l, F d, Y g:i:s A",
				MonthDay: "F d",
				ShortTime: "g:i A",
				LongTime: "g:i:s A",
				SortableDateTime: "Y-m-d\\TH:i:s",
				UniversalSortableDateTime: "Y-m-d H:i:sO",
				YearMonth: "F, Y"
			},
			reformatAfterEdit : false
		},
		baseLinkUrl: '',
		showAction: '',
		target: '',
		checkbox : {disabled:true},
		idName : 'id'
	}
};
})(jQuery);
