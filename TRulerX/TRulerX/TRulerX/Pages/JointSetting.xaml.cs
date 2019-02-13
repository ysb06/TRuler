using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

using Xamarin.Forms;
using Xamarin.Forms.Xaml;

namespace TRulerX.Pages
{
	[XamlCompilation(XamlCompilationOptions.Compile)]
	public partial class JointSetting : ContentPage
	{
		public JointSetting ()
		{
			InitializeComponent ();

            //사진 비율에 따라 LayoutBound 수정
            
		}
	}
}