# connector-racf-zowe
midPoint connector for IBM Mainframe RACF using Zowe.

Close to midPoint is an usual Java connector, that uses REST to call services running on Zato (https://zato.io/), which call Zowe API (https://www.zowe.org/) to send RACF commands through TSO Sessions except Search All, that calls IRRDBU00 unload job, download generated report and them parse it with PyRACF (https://github.com/wizardofzos/pyracf) into Panda DataFrames for every recordtype.

It supports regular RACF user account management plus TSO and OMVS classes/recordtypes. Roadmap includes CICS and Netview classes too.
