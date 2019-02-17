using System;
using System.Collections.Generic;
using System.Text;
using System.Diagnostics;

using Xamarin.Forms;

using Plugin.Media.Abstractions;

using TRulerX.Ergonomics;


namespace TRulerX.Phase.PhaseTools
{
    public class AnalysisSheet : IDisposable
    {
        public AnalysisSheet()
        {

        }
        public AnalysisSheet(bool IsDebugging)
        {
            if(IsDebugging)
            {
                Worker = "John Doe";
                Location = "인도 뭄바이 시청";
                Crop = "옥수수";
                Work = "들기";
                WorkDetatiled = "잡아 들기";
                Weight = "아주 무거움 10kg";
                Grab = "손잡이";
                Time = 5;
                Analyzer = "Zack Snyder";
                NeckBend = "보통";

                ImagePath = "test.jpg";

                Upper = UpperPosture.B0_S0_E45;
                Lower = LowerPosture.STD;
                FullPosturePath = "posture_00_00.png";
                UpperRisk = 1;
                LowerRisk = 1;
                UpperTimeRisk = 1;
                LowerTimeRisk = 1;
            }
        }

        #region Phase 1
        public string Worker { get; set; }
        public string Location { get; set; }
        public string Crop { get; set; }
        public string Work { get; set; }
        public string WorkDetatiled { get; set; }
        public string Weight { get; set; }
        public string Grab { get; set; }
        public int Time { get; set; }
        public string Analyzer { get; set; }
        public string NeckBend { get; set; }
        #endregion
        #region Phase 2
        public string ImagePath { get; set; }
        private MediaFile imageFile;
        public MediaFile ImageFile {
            get
            {
                if(imageFile == null)
                {
                    Debug.WriteLine("Warning: Image File is null");
                }
                return imageFile;
            }
            set
            {
                if (imageFile != null)
                {
                    Debug.WriteLine(imageFile.Path + " is Disposing");
                    imageFile.Dispose();
                }
                imageFile = value;
            }
        }
        public double ImageRotationCorrection { get; set; }
        #endregion
        #region Phase 3
        public Point[] JointPoint { get; set; }
        #endregion
        #region Phase 4
        public UpperPosture Upper { get; set; }
        public LowerPosture Lower { get; set; }
        public string FullPosturePath { get; set; }
        public int UpperRisk { get; set; }      //주의 1~4
        public int LowerRisk { get; set; }      //주의 1~4
        public int UpperTimeRisk { get; set; }
        public int LowerTimeRisk { get; set; }
        public bool[] DangerBodyPart { get; set; }
        public int OverallRisk { get; set; }
        public int OverallTimeRisk { get; set; }
        #endregion



        public void Dispose()
        {
            if (imageFile != null)
            {
                imageFile.Dispose();
            }
        }
    }
}
