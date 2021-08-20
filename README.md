# Ozark Client

![logo](https://github.com/Trambled/OzarkClient/blob/master/src/main/resources/ozark%20horizontal.png)
  
  https://www.ozarkclient.ga/
    

## Contributing/build (only for pro coders)
1. Clone the Repository under `https://github.com/Trambled/OzarkClient`
2. Open command prompt and CD (cds nut idiot) into the directory.
3. Depending on which IDEA you use, different commands have to be ran.
    - IntelliJ: `gradlew setupDecompWorkspace idea genIntellijRuns build`
    - Eclipse: `gradlew setupDecompWorkspace eclipse build`
    - if you only want to build do `gradlew setupDecompWorkspace build` and look for build > libs > then you should see three .jar files, the one that has -all in end of it
      is the jar file you should use.
4. Open the folder in your preferred IDEA, depending which you chose above.
6. And change what you need to change in your IDEA and click "Git" on top of your screen and commit.

You also need a JDK, a free one is adopt open JDK https://adoptopenjdk.net/ and gradle to build

## Downloads 

Download the latest and stable release from [here](https://github.com/Trambled/OzarkClient/releases)

## How to use

What's the default bind for ClickGUI?

`R-SHIFT`

Default Command Prefix?

`.` is the prefix

## Details and stuff

<details>
  <summary>Notable stuff added.</summary> <br>
  Bed Aura - Auto places and breaks beds on people, has break calculations and has modes for 1.13 and 1.12 servers. <br>
  Auto Crystal - Very good and heavily modified w+2 autocrystal. <br>
  BurrowESP - Highlights people who are burrowed. <br>
  Elytrafly - Salhack but timer on takeoff. <br>
  Burrow - Xulu but modified a bit to make it better. <br>
  Xray - With commands and opacity feature. <br>
  PastGUI - Another GUI originally from past. <br>
  Anticrystal - Originally from xenon but added minhealth and crystal calculations to make it better, also supports string.<br>
  Instantburrow - made by ObsidianBreaker from nekohax and leux but added to ozark. <br>
  Some other skidded shit and bug fixes. <br><br>
</details>

## Configuring

<details>

<details>
  <summary>trampled autocrystal config</summary> <br>
  
CaSetting:Rotations <br> 
CaDebug:false <br> 
CaPlace:true <br> 
CaBreak:true <br> 
CaAntiWeakness:true <br> 
CaAlternative:false <br> 
CaModuleCheck:true <br> 
CaBreakPredict:true <br> 
CaPlacePredict:false <br> 
CaSoundPredict:false <br> 
CaCityPredict:true <br> 
CaMotionPredict:true <br> 
CaVerifyPlace:false <br> 
CaInhibit:false <br> 
CaInhibitDelay:0 <br> 
CaInhibitSwings:50 <br> 
CaBreakAttempts:1 <br> 
CaPlaceAttempts:1 <br> 
CaHitRange:5.0 <br> 
CaPlaceRange:5.0 <br> 
CaHitRangeWall:3.5 <br> 
CaPlaceRangeWall:3.5 <br> 
CaPlayerRange:10 <br> 
CaPlaceDelay:0 <br> 
CaBreakDelay:1 <br> 
CaMinEnemyPlace:6 <br> 
CaMinEnemyBreak:6 <br> 
CaMaxSelfDamage:8 <br> 
CaMinHealthPause:true <br> 
CaRequiredHealth:1 <br> 
CaWebIgnore:true <br> 
CaPacketPlace:true <br> 
CaPacketBreak:true <br> 
CaTargetMode:Health <br> 
CaRaytrace:false <br> 
CaSwitchMode:None <br> 
CaAntiSuicide:true <br> 
CaFastMode:true <br> 
CaFastPlace:true <br> 
CaBreakAll:false <br> 
CaMomentumMode:true <br> 
CaSync:Sound <br> 
CaHeuristic:Distance <br> 
CaHeuristicMinHealth:12 <br> 
CaAntiStuck:true <br> 
CaAntiStuckTries:1 <br> 
CaAntiStuckTime:1000 <br> 
CaThirteen:false <br> 
CaMultiplace:false <br> 
CaTabbottMode:true <br> 
CaTabbottModeHealth:10 <br> 
CaJumpyFaceMode:false <br> 
CaArmorDestroy:true <br> 
CaArmorPercent:25 <br> 
CaArmorPercentSelf:0 <br> 
CaStopWhileMining:false <br> 
CaStopWhileEatin:false <br> 
CaSwing:Offhand <br> 
CaRotateMode:Packet <br> 
CaRotateDuring:BOTH <br> 
CaRotateAntiWaste:false <br> 
CaRotateLimiter:None <br> 
CaMaxAngle:0.0 <br> 
CaMinAngle:360.0 <br> 
CaRandomRotate:false <br> 
CaQueue:true <br> 
CaAccurate:true <br> 
CaRotateDetectRubberband:false <br> 
CaRestoreRotationInstant:false <br> 
CaSolid:true <br> 
CaOutline:false <br> 
CaGlowSolid:false <br> 
CaGlowOutline:false <br> 
CaOldRender:false <br> 
CaFutureRender:false <br> 
CaTopBlock:false <br> 
CaR:255 <br> 
CaG:149 <br> 
CaB:0 <br> 
CaA:255 <br> 
CaOutlineA:255 <br> 
CaGlowA:0 <br> 
CaGlowOutlineA:0 <br> 
CaRainbow:true <br> 
CaSatiation:1.0 <br> 
CaBrightness:1.0 <br> 
CaHeight:1.0 <br> 
CaRenderDamage:Normal <br> 
CaCleanMode:true <br> 
CaSwitchBind:0 <br> 
CaFaceBind:0 <br> 
</details>  
turn rotate mode to none if you want
  
all other configs posted in the disc

Offhand health is based on preference but 16-17 is the best u can get for an offhand to rarely fail without a crystal check anything higher is pretty unnecessary unless u get like 500+ ping or something. Also if ur so scared of totfail turn on crystal check and set damage multiplier to ~1.06 - ~1.50

Elytrafly if it lags back with the default speed try lowering it, Setting the glide speed higher might also fix it, Also Elytrafly isn't compatible with nofall, Thats why Ozark Elytrafly auto toggles nofall, So make sure that no other clients have the nofall module toggled when you're using Elytrafly. Elytrafly also can bug out with strafe enabled.

## Capes
you can give cape designs and i can add u to the pastebin of uuids for capes.
