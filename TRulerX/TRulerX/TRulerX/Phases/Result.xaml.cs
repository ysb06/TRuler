using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using System.Diagnostics;
using System.IO;

using Xamarin.Forms;
using Xamarin.Forms.Xaml;

using SkiaSharp;

using TRulerX.Phase.PhaseTools;
using TRulerX.Ergonomics;

namespace TRulerX.Phase
{
    public enum ResultState { Overall, OverallTime, Upper, Lower }

	[XamlCompilation(XamlCompilationOptions.Compile)]
	public partial class Result : ContentPage
	{
        private const double OVERALL_TABLE_ASPECT = 487d / 313d;
        private const double SPECIFIC_TABLE_ASPECT = 895d / 366d;

        private readonly Point OVERALL_TABLE_ORIGIN = new Point(230, 152);
        private const double OVERALL_TABLE_HORIZONTAL_INTERVAL = 73d;
        private const double OVERALL_TABLE_VERTICAL_INTERVAL = 45d;

        private readonly Point UPPER_TABLE_ORIGIN = new Point(80, 192);
        private const double UPPER_TABLE_HORIZONTAL_INTERVAL = 61d;
        private const double UPPER_TABLE_VERTICAL_INTERVAL = 49d;

        private readonly Point LOWER_TABLE_ORIGIN = new Point(85, 192);
        private const double LOWER_TABLE_HORIZONTAL_INTERVAL = 65d;
        private const double LOWER_TABLE_VERTICAL_INTERVAL = 49d;

        private const float CIRCLE_SIZE = 75;

        private AnalysisSheet sheet;

        private ResultState currentState = ResultState.Overall;

        private SKPaint pCircle = new SKPaint
        {
            Style = SKPaintStyle.Stroke,
            StrokeWidth = 20,
            Color = SKColors.RoyalBlue,
        };

        public Result()
		{
			InitializeComponent ();
            sheet = new AnalysisSheet();
		}

        public Result(AnalysisSheet sheet)
        {
            InitializeComponent();
            this.sheet = sheet;
            MarkerBoard.PaintSurface += MarkerBoard_PaintSurface;
        }

        private void MarkerBoard_PaintSurface(object sender, SkiaSharp.Views.Forms.SKPaintSurfaceEventArgs e)
        {
            SKImageInfo info = e.Info;
            SKSurface surface = e.Surface;
            SKCanvas canvas = surface.Canvas;

            canvas.Clear();

            double x = 0;
            double y = 0;
            int posture = (int)sheet.Upper;
            
            switch(currentState)
            {
                case ResultState.Overall:
                    x = OVERALL_TABLE_ORIGIN.X + OVERALL_TABLE_HORIZONTAL_INTERVAL * (4 - sheet.UpperRisk);
                    y = OVERALL_TABLE_ORIGIN.Y + OVERALL_TABLE_VERTICAL_INTERVAL * (4 - sheet.LowerRisk);
                    x = x / 487d * ScoreBoard.Width;
                    y = y / 313d * ScoreBoard.Height;
                    break;
                case ResultState.OverallTime:
                    x = OVERALL_TABLE_ORIGIN.X + OVERALL_TABLE_HORIZONTAL_INTERVAL * (4 - sheet.UpperTimeRisk);
                    y = OVERALL_TABLE_ORIGIN.Y + OVERALL_TABLE_VERTICAL_INTERVAL * (4 - sheet.LowerTimeRisk);  //시간 위험 값으로...
                    x = x / 487d * ScoreBoard.Width;
                    y = y / 313d * ScoreBoard.Height;
                    break;
                case ResultState.Upper:
                    x = UPPER_TABLE_ORIGIN.X + UPPER_TABLE_HORIZONTAL_INTERVAL * (int)sheet.Upper;
                    y = UPPER_TABLE_ORIGIN.Y + UPPER_TABLE_VERTICAL_INTERVAL * (sheet.UpperTimeRisk - 1);
                    x = x / 895d * ScoreBoard.Width;
                    y = y / 366d * ScoreBoard.Height;
                    break;
                case ResultState.Lower:
                    x = LOWER_TABLE_ORIGIN.X + LOWER_TABLE_HORIZONTAL_INTERVAL * (int)sheet.Lower;
                    y = LOWER_TABLE_ORIGIN.Y + LOWER_TABLE_VERTICAL_INTERVAL * (sheet.LowerTimeRisk - 1);
                    x = x / 895d * ScoreBoard.Width;
                    y = y / 366d * ScoreBoard.Height;
                    break;
            }
            canvas.DrawCircle(ConvertToPixel(new Point(x, y)), CIRCLE_SIZE, pCircle);
        }

        private SKPoint ConvertToPixel(Point pt)
        {
            return new SKPoint((float)(MarkerBoard.CanvasSize.Width * pt.X / MarkerBoard.Width),
                               (float)(MarkerBoard.CanvasSize.Height * pt.Y / MarkerBoard.Height));
        }

        public void OnActivated()
        {
            PoseForm form = new PoseForm()
            {
                Time = sheet.Time,
                UpperPosture = sheet.Upper,
                LowerPosture = sheet.Lower
            };
            RiskAnalyzer riskAnalyzer = new RiskAnalyzer(form);

            sheet.UpperRisk = riskAnalyzer.UpperPostureRisk;
            sheet.UpperTimeRisk = riskAnalyzer.UpperPostureTimeRisk;
            sheet.LowerRisk = riskAnalyzer.LowerPostureRisk;
            sheet.LowerTimeRisk = riskAnalyzer.LowerPostureTimeRisk;
            sheet.DangerBodyPart = riskAnalyzer.DangerPart;
            sheet.OverallRisk = riskAnalyzer.OverallRisk;
            sheet.OverallTimeRisk = riskAnalyzer.OverallTimeRisk;

            Debug.WriteLine("작업시간    : " + sheet.Time);
            Debug.WriteLine("상지자세    : " + sheet.Upper);
            Debug.WriteLine("상지위험    : " + sheet.UpperRisk);
            Debug.WriteLine("상지시간위험: " + sheet.UpperTimeRisk);

            Debug.WriteLine("하지자세    : " + sheet.Lower);
            Debug.WriteLine("하지위험    : " + sheet.LowerRisk);
            Debug.WriteLine("하지시간위험: " + sheet.LowerTimeRisk);

            Debug.WriteLine("종합위험    : " + sheet.OverallRisk);
            Debug.WriteLine("종합시간위험: " + sheet.OverallTimeRisk);

            ShowOverall();

            //공통 및 변화 없는 작업
            //부담 위치 설정
            //상지
            UpperRiskPart.Text = "";
            if(sheet.DangerBodyPart[0])
            {
                UpperRiskPart.Text += "목 ";
            }

            if (sheet.DangerBodyPart[1])
            {
                UpperRiskPart.Text += "어깨 ";
            }

            if (sheet.DangerBodyPart[2])
            {
                UpperRiskPart.Text += "팔꿈치 ";
            }
            if(!sheet.DangerBodyPart[0] && !sheet.DangerBodyPart[1] && !sheet.DangerBodyPart[2])
            {
                UpperRiskPart.Text += "상지 부담 작업 없음";
            }
            else
            {
                UpperRiskPart.Text += "부담 작업";
            }

            //하지
            LowerRiskPart.Text = "";
            if (sheet.DangerBodyPart[3])
            {
                LowerRiskPart.Text += "허리 ";
            }

            if (sheet.DangerBodyPart[4])
            {
                LowerRiskPart.Text += "무릎 ";
            }

            if (sheet.DangerBodyPart[5])
            {
                LowerRiskPart.Text += "발목 ";
            }
            if (!sheet.DangerBodyPart[3] && !sheet.DangerBodyPart[4] && !sheet.DangerBodyPart[5])
            {
                LowerRiskPart.Text += "하지 부담 작업 없음";
            }
            else
            {
                LowerRiskPart.Text += "부담 작업";
            }


            PoseResult.Source = ImageSource.FromFile(PostureAnalyzer.GetFullPoseImageFileName(sheet.Upper, sheet.Lower));
        }

        private void ShowOverall()
        {
            currentState = ResultState.Overall;
            ScoreBoard.Source = ImageSource.FromFile("tables2.png");

            //표 완성

            UpperRiskLevel.Text = sheet.UpperRisk.ToString();
            LowerRiskLevel.Text = sheet.LowerRisk.ToString();
            
            //표 작업 끝

            ScoreBoardFrame.WidthRequest = (OVERALL_TABLE_ASPECT * ScoreBoard.Height);
            Debug.WriteLine("Width: " + ScoreBoardFrame.Width);
            MarkerBoard.InvalidateSurface();

            VisualStateManager.GoToState(OverallButton, "Selected");
            VisualStateManager.GoToState(OverallTimeButton, "Normal");
            VisualStateManager.GoToState(UpperButton, "Normal");
            VisualStateManager.GoToState(LowerButton, "Normal");
        }

        private void ShowTimeOverall()
        {
            currentState = ResultState.OverallTime;
            ScoreBoard.Source = ImageSource.FromFile("tables2.png");

            //표 완성

            UpperRiskLevel.Text = sheet.UpperTimeRisk.ToString();
            LowerRiskLevel.Text = sheet.LowerTimeRisk.ToString();

            //표 작업 끝

            ScoreBoardFrame.WidthRequest = (OVERALL_TABLE_ASPECT * ScoreBoard.Height);
            Debug.WriteLine("Width: " + ScoreBoardFrame.Width);
            MarkerBoard.InvalidateSurface();

            VisualStateManager.GoToState(OverallButton, "Normal");
            VisualStateManager.GoToState(OverallTimeButton, "Selected");
            VisualStateManager.GoToState(UpperButton, "Normal");
            VisualStateManager.GoToState(LowerButton, "Normal");
        }

        private void ShowUpperInfo()
        {
            currentState = ResultState.Upper;
            ScoreBoard.Source = ImageSource.FromFile("tableuv2u.png");

            //표 완성

            UpperRiskLevel.Text = sheet.UpperTimeRisk.ToString();
            LowerRiskLevel.Text = sheet.LowerTimeRisk.ToString();

            //표 작업 끝

            ScoreBoardFrame.WidthRequest = (SPECIFIC_TABLE_ASPECT * ScoreBoard.Height);
            Debug.WriteLine("Width: " + ScoreBoardFrame.Width);
            MarkerBoard.InvalidateSurface();

            VisualStateManager.GoToState(OverallButton, "Normal");
            VisualStateManager.GoToState(OverallTimeButton, "Normal");
            VisualStateManager.GoToState(UpperButton, "Selected");
            VisualStateManager.GoToState(LowerButton, "Normal");
        }

        private void ShowLowerInfo()
        {
            currentState = ResultState.Lower;
            ScoreBoard.Source = ImageSource.FromFile("tableuv3l.png");

            //표 완성

            UpperRiskLevel.Text = sheet.UpperTimeRisk.ToString();
            LowerRiskLevel.Text = sheet.LowerTimeRisk.ToString();

            //표 작업 끝

            ScoreBoardFrame.WidthRequest = (SPECIFIC_TABLE_ASPECT * ScoreBoard.Height);
            Debug.WriteLine("Width: " + ScoreBoardFrame.Width);
            MarkerBoard.InvalidateSurface();

            VisualStateManager.GoToState(OverallButton, "Normal");
            VisualStateManager.GoToState(OverallTimeButton, "Normal");
            VisualStateManager.GoToState(UpperButton, "Normal");
            VisualStateManager.GoToState(LowerButton, "Selected");
        }

        private string GetBodyPartString(BodyPart bodyPart)
        {
            switch(bodyPart)
            {
                case BodyPart.Neck:
                    return "목";
                case BodyPart.Shoulder:
                    return "어깨";
                case BodyPart.Elbow:
                    return "손";
                case BodyPart.Back:
                    return "등";
                case BodyPart.Knee:
                    return "무릎";
                default:
                    return "";
            }
        }

        private void Button_Clicked(object sender, EventArgs e)
        {
            if (sender == OverallButton)
            {
                ShowOverall();
            }

            if (sender == OverallTimeButton)
            {
                ShowTimeOverall();
            }

            if (sender == UpperButton)
            {
                ShowUpperInfo();
            }

            if (sender == LowerButton)
            {
                ShowLowerInfo();
            }
        }

        private void Service_Button_Clicked(object sender, EventArgs e)
        {
            if(sender == VideoButton)
            {
                Navigation.PushAsync(new VideoPage(sheet.DangerBodyPart));
            }

            if(sender == FileButton)
            {
                SaveData();
            }
        }

        private async void SaveData()
        {
            DateTime currentDate = DateTime.Now;
            StringBuilder builder = new StringBuilder(
                "작업자,지역,작목,작업,세부작업,무게,드는부위,유지시간,분석자,목젖힘정도" +
                "분석일자,상지자세,상지위험도,상지시간위험도,하지자세,하지위험도,하지시간위험도,종합위험도,종합시간위험도\n"
                );
            builder.Append(sheet.Worker);   
            builder.Append(',');            //작업자
            builder.Append(sheet.Location);   
            builder.Append(',');            //지역
            builder.Append(sheet.Crop);
            builder.Append(',');            //작목
            builder.Append(sheet.Work);
            builder.Append(',');            //작업
            builder.Append(sheet.WorkDetatiled);
            builder.Append(',');            //세부작업
            builder.Append(sheet.Weight);
            builder.Append(',');            //무게
            builder.Append(sheet.Grab);
            builder.Append(',');            //드는부위
            builder.Append(sheet.Time);
            builder.Append(',');            //유지시간
            builder.Append(sheet.Analyzer);
            builder.Append(',');            //분석자
            builder.Append(sheet.NeckBend);
            builder.Append(',');            //목젖힘정도
            builder.Append(currentDate);
            builder.Append(',');            //분석일자
            builder.Append(sheet.Upper);
            builder.Append(',');            //상지자세
            builder.Append(sheet.UpperRisk);
            builder.Append(',');            //상지위험도
            builder.Append(sheet.UpperTimeRisk);
            builder.Append(',');            //상지시간위험도
            builder.Append(sheet.Lower);
            builder.Append(',');            //하지자세
            builder.Append(sheet.LowerRisk);
            builder.Append(',');            //하지위험도
            builder.Append(sheet.LowerTimeRisk);
            builder.Append(',');            //하지시간위험도
            builder.Append(sheet.OverallRisk);
            builder.Append(',');            //종합 위험도
            builder.Append(sheet.OverallRisk);
            //종합 시간 위험도

            //StringBuilder filenameBuilder = new StringBuilder(Environment.GetFolderPath(Environment.SpecialFolder.MyDocuments));
            StringBuilder filenameBuilder = new StringBuilder("/storage/emulated/0/Context/");
            if (!Directory.Exists(filenameBuilder.ToString()))
            {
                Directory.CreateDirectory(filenameBuilder.ToString());
                Debug.WriteLine("Folder Created");
            }
            filenameBuilder.Append(currentDate.Year);
            filenameBuilder.Append(currentDate.Month);
            filenameBuilder.Append(currentDate.Day);
            filenameBuilder.Append(currentDate.Hour);
            filenameBuilder.Append(currentDate.Minute);
            filenameBuilder.Append(currentDate.Second);
            filenameBuilder.Append("Pose.csv");

            File.WriteAllText(filenameBuilder.ToString(), builder.ToString(), Encoding.UTF8);
            filenameBuilder.Append("에 저장되었습니다.");
            await DisplayAlert("File Location", filenameBuilder.ToString(), "OK");
        }
    }
}