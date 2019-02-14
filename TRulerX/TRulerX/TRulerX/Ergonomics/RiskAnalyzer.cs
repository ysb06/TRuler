using System;
using System.Collections.Generic;
using System.Text;
using System.Diagnostics;

namespace TRulerX.Ergonomics
{
    

    public class RiskAnalyzer
    {
        public string UpperPoseRisk { get; private set; }
        private int B = 0;
        private int S = 0;
        private int E = 0;
        public string LowerPoseRisk { get; private set; }
        private int A = 0;

        public int UpperPoseRiskLevel { get; private set; }
        public int LowerPoseRiskLevel { get; private set; }

        public RiskAnalyzer(Skeleton skeleton)
        {
            int backAngle = skeleton.BackAngleGroup;
            int shoulderAngle = skeleton.ShoulderAngleGroup;
            int elbowAngle = skeleton.ElbowAngleGroup;

            if(backAngle > 90)
            {
                backAngle = 90;
            }

            B = backAngle;
            S = shoulderAngle;
            E = elbowAngle;

            A = skeleton.AnkleAngleGroup;

            UpperPoseRisk = "B" + skeleton.BackAngleGroup + "-S" + skeleton.ShoulderAngleGroup + "-E" + skeleton.ElbowAngleGroup;
            LowerPoseRisk = "A" + skeleton.AnkleAngleGroup;

            //상지 120은 90으로 처리

        }

        private void CalcUpperRiskLevel()
        {
            if (B == 0 && S == 0 && E == 45)        { UpperPoseRiskLevel = 1; }
            else if (B == 0 && S == 0 && E == 90)   { UpperPoseRiskLevel = 1; }
            else if (B == 0 && S == 45 && E == 0)   { UpperPoseRiskLevel = 2; }
            else if (B == 0 && S == 45 && E == 45)  { UpperPoseRiskLevel = 2; }
            else if (B == 0 && S == 45 && E == 90)  { UpperPoseRiskLevel = 2; }
            else if (B == 0 && S == 90 && E == 45)  { UpperPoseRiskLevel = 3; }
            else if (B == 0 && S == 90 && E == 90)  { UpperPoseRiskLevel = 3; }
            else if (B == 0 && S == 125 && E == 0)  { UpperPoseRiskLevel = 4; }
            else if (B == 45 && S == 45 && E == 0)  { UpperPoseRiskLevel = 2; }
            else if (B == 45 && S == 45 && E == 45) { UpperPoseRiskLevel = 3; }
            else if (B == 45 && S == 90 && E == 0)  { UpperPoseRiskLevel = 3; }
            else if (B == 45 && S == 90 && E == 45) { UpperPoseRiskLevel = 3; }
            else if (B == 90 && S == 90 && E == 0)  { UpperPoseRiskLevel = 4; }
            else if (B == 90 && S == 90 && E == 45) { UpperPoseRiskLevel = 4; }
            else
            {
                Debug.WriteLine("No defined upper risk");
                UpperPoseRiskLevel = -1;
            }
        }

        private void CalcLowerRiskLevel()
        {
            if (A == 45) { LowerPoseRiskLevel = 4; }        //스쿼팅 부분 세분화 할 것
            else if (A == 90) { LowerPoseRiskLevel = 4; }
            else if (A == 125) { LowerPoseRiskLevel = 4; }
            else if (A == 150) { LowerPoseRiskLevel = 3; }
            else if (A == 180) { LowerPoseRiskLevel = 1; }
            else
            {
                Debug.WriteLine("No defined lower risk");
                LowerPoseRiskLevel = -1;
            }
        }

        private void RecognizeLowerRisk()
        {
            //STD 등 판단하는 부분
        }
    }
}
