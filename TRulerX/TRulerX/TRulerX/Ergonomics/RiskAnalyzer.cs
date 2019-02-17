using System;
using System.Collections.Generic;
using System.Text;
using System.Diagnostics;

namespace TRulerX.Ergonomics
{
    public class RiskAnalyzer
    {
        //readonly private Dictionary<UpperPosture, int> upperPostureRiskMap = new Dictionary<UpperPosture, int>();
        //readonly private Dictionary<LowerPosture, int> lowerPostureRiskMap = new Dictionary<LowerPosture, int>();

        private readonly int[] upperPostureRiskMap = { 1, 1, 2, 2, 2, 3, 3, 4, 2, 3, 3, 3, 4, 4};
        private int[] lowerPostureRiskMap = { 1, 3, 4, 4, 4, 3, 3, 3, 3, 1, 1, 2, 1 };

        private readonly int[,] upperPostureTimeRiskMapRaw =
        {
            {  3,  2, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1 },
            {  8,  7,  2,  2,  1, -1, -1, -1,  1, -1, -1, -1,  1,  1 },
            { 17, 14,  9,  8,  5,  2,  2, -1,  2,  5,  2,  4,  6,  5 }
        };
        private readonly int[,] lowerPostureTimeRiskMapRaw =
        {
            {  2, -1, -1, -1, -1, -1, -1, -1, -1,  2,  1,  2,  2},
            { 11,  8, -1, -1, -1,  2,  2,  2,  2, 11,  2, 13, 18},
            { 33, 12, -1, -1, -1,  9,  3,  4,  5, 35, 18, 38, 57}
        };

        private readonly int[,] overallRiskMap =
        {
            { 4, 4, 4, 3 },
            { 4, 3, 3, 3 },
            { 4, 3, 2, 2 },
            { 3, 3, 2, 1 }
        };

        public readonly int UpperPostureRisk = 0;
        public readonly int LowerPostureRisk = 0;
        public readonly int UpperPostureTimeRisk = 0;
        public readonly int LowerPostureTimeRisk = 0;
        public readonly int OverallRisk = 0;
        public readonly int OverallTimeRisk = 0;
        public bool[] DangerPart = new bool[6];

        public RiskAnalyzer(PoseForm form)
        {
            UpperPostureRisk = upperPostureRiskMap[(int)form.UpperPosture];
            LowerPostureRisk = lowerPostureRiskMap[(int)form.LowerPosture];
            //*
            //상지
            if (form.Time > upperPostureTimeRiskMapRaw[2, (int)form.UpperPosture])
            {
                UpperPostureTimeRisk = 4;
            }
            else if(form.Time > upperPostureTimeRiskMapRaw[1, (int)form.UpperPosture])
            {
                UpperPostureTimeRisk = 3;
            }
            else if(form.Time > upperPostureTimeRiskMapRaw[0, (int)form.UpperPosture])
            {
                UpperPostureTimeRisk = 2;
            }
            else
            {
                UpperPostureTimeRisk = 1;
            }

            //하지
            if (form.Time > lowerPostureTimeRiskMapRaw[2, (int)form.LowerPosture])
            {
                LowerPostureTimeRisk = 4;
            }
            else if (form.Time > lowerPostureTimeRiskMapRaw[1, (int)form.LowerPosture])
            {
                LowerPostureTimeRisk = 3;
            }
            else if (form.Time > lowerPostureTimeRiskMapRaw[0, (int)form.LowerPosture])
            {
                LowerPostureTimeRisk = 2;
            }
            else
            {
                LowerPostureTimeRisk = 1;
            }
            //*/
            SetDangerPart(form.UpperPosture, form.LowerPosture);

            OverallRisk = overallRiskMap[LowerPostureRisk - 1, UpperPostureRisk - 1];
            OverallTimeRisk = overallRiskMap[LowerPostureTimeRisk - 1, UpperPostureTimeRisk - 1];
        }

        //상지(0~2) 목, 어깨, 팔꿈치
        //하지(3~4) 허리, 무릎
        private void SetDangerPart(UpperPosture upper, LowerPosture lower)
        {
            for (int i = 0; i < DangerPart.Length; i++)
            {
                DangerPart[i] = false;
            }

            switch(upper)
            {
                case UpperPosture.B0_S0_E45:
                case UpperPosture.B0_S0_E90:
                    DangerPart[2] = true;
                    break;
                case UpperPosture.B0_S45_E0:
                case UpperPosture.B0_S45_E90:
                case UpperPosture.B0_S120_E0:
                    DangerPart[1] = true;
                    break;
                case UpperPosture.B0_S45_E45:
                case UpperPosture.B0_S90_E45:
                case UpperPosture.B0_S90_E90:
                    DangerPart[1] = true;
                    DangerPart[2] = true;
                    break;
                case UpperPosture.B45_S45_E0:
                case UpperPosture.B90_S90_E0:
                    DangerPart[3] = true;
                    break;
                case UpperPosture.B45_S45_E45:
                case UpperPosture.B45_S90_E0:
                case UpperPosture.B45_S90_E45:
                    DangerPart[1] = true;
                    DangerPart[3] = true;
                    break;
                case UpperPosture.B90_S90_E45:
                    DangerPart[2] = true;
                    DangerPart[3] = true;
                    break;
            }
            //상지(0~2) 목, 어깨, 팔꿈치
            //하지(3~4) 허리, 무릎
            switch(lower)
            {
                case LowerPosture.KF150:
                case LowerPosture.KF120:
                    DangerPart[4] = true;
                    break;
                case LowerPosture.KF90:
                case LowerPosture.KF60:
                case LowerPosture.KF30:
                case LowerPosture.KNL1:
                case LowerPosture.KNL2:
                    DangerPart[3] = true;
                    DangerPart[4] = true;
                    break;
                case LowerPosture.KF30C:
                    DangerPart[5] = true;
                    break;
            }
        }
    }

    public struct PoseForm
    {
        public int Time;
        public UpperPosture UpperPosture;
        public LowerPosture LowerPosture;
    }
}
