package com.gmail.jorgegilcavazos.ballislife.features.gamedetail

import android.support.constraint.ConstraintLayout
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.Switch
import android.widget.TextView
import com.gmail.jorgegilcavazos.ballislife.R
import com.gmail.jorgegilcavazos.ballislife.features.gamedetail.GameSummaryUIView.GameState.LIVE
import com.gmail.jorgegilcavazos.ballislife.features.gamedetail.GameSummaryUIView.GameState.POST
import com.gmail.jorgegilcavazos.ballislife.features.gamedetail.GameSummaryUIView.GameState.PRE
import com.gmail.jorgegilcavazos.ballislife.features.gamedetail.GameSummaryUIView.GameSummaryUiEvent.BackPressed
import com.gmail.jorgegilcavazos.ballislife.features.gamedetail.GameSummaryUIView.GameSummaryUiEvent.DelayPressed
import com.gmail.jorgegilcavazos.ballislife.features.gamedetail.GameSummaryUIView.GameSummaryUiEvent.StreamChecked
import com.gmail.jorgegilcavazos.ballislife.features.gamedetail.GameSummaryUIView.GameSummaryUiEvent.StreamUnchecked
import com.gmail.jorgegilcavazos.ballislife.features.model.Team
import com.gmail.jorgegilcavazos.ballislife.util.TeamUtils
import com.jakewharton.rxrelay2.PublishRelay
import io.reactivex.Observable

class GameSummaryUIView(parent: ViewGroup) {

  private val view: View =
      LayoutInflater.from(parent.context).inflate(R.layout.game_detail_header, parent, false)

  private val backBtn: ImageButton = view.findViewById(R.id.backButton)
  private val streamSwitch: Switch = view.findViewById(R.id.streamSwitch)
  private val delayBtn: ImageButton = view.findViewById(R.id.delayButton)

  private val homeTeamInfo: View = view.findViewById(R.id.home)
  private val homeLogo: ImageView = homeTeamInfo.findViewById(R.id.logo)
  private val homeName: TextView = homeTeamInfo.findViewById(R.id.name)

  private val visitorTeamInfo: View = view.findViewById(R.id.visitor)
  private val visitorLogo: ImageView = visitorTeamInfo.findViewById(R.id.logo)
  private val visitorName: TextView = visitorTeamInfo.findViewById(R.id.name)

  private val gameStatus: ConstraintLayout = view.findViewById(R.id.gameStatus)
  private val homeScore: TextView = gameStatus.findViewById(R.id.homeScore)
  private val visitorScore: TextView = gameStatus.findViewById(R.id.visitorScore)
  private val clock: TextView = gameStatus.findViewById(R.id.clock)
  private val period: TextView = gameStatus.findViewById(R.id.period)
  private val startTime: TextView = gameStatus.findViewById(R.id.startTime)
  private val broadcaster: TextView = gameStatus.findViewById(R.id.broadcaster)
  private val halftimeText: TextView = gameStatus.findViewById(R.id.halftime)

  private val uiEvents = PublishRelay.create<GameSummaryUiEvent>()

  init {
    parent.addView(view)

    backBtn.setOnClickListener { uiEvents.accept(BackPressed) }
    streamSwitch.setOnCheckedChangeListener { _, isChecked ->
      if (isChecked) {
        uiEvents.accept(StreamChecked)
      } else {
        uiEvents.accept(StreamUnchecked)
      }
    }
    delayBtn.setOnClickListener { uiEvents.accept(DelayPressed) }

    gameStatus.loadLayoutDescription(R.xml.game_detail_game_status_states)
  }

  fun getUiEvents(): Observable<GameSummaryUiEvent> = uiEvents

  fun setHomeTeamInfo(team: Team) {
    homeLogo.setImageResource(TeamUtils.getTeamLogo(team.key))
    homeName.text = team.key
  }

  fun setVisitorTeamInfo(team: Team) {
    visitorLogo.setImageResource(TeamUtils.getTeamLogo(team.key))
    visitorName.text = team.key
  }

  fun setGameState(gameState: GameState) {
    val layoutState = when (gameState) {
      PRE -> R.id.pre
      LIVE -> R.id.live
      POST -> R.id.post
    }
    gameStatus.setState(layoutState, 0, 0)
  }

  fun setHomeScore(score: String) {
    homeScore.text = score
  }

  fun setVisitorScore(score: String) {
    visitorScore.text = score
  }

  fun setScoresToHyphen() {
    homeScore.text = "-"
    visitorScore.text = "-"
  }

  fun setClockVisibility(visible: Boolean) {
    clock.visibility = if (visible) View.VISIBLE else View.GONE
  }

  fun setClock(clock: String) {
    this.clock.text = clock
  }

  fun setPeriodVisibility(visible: Boolean) {
    period.visibility = if (visible) View.VISIBLE else View.GONE
  }

  fun setPeriod(period: String) {
    this.period.text = period
  }

  fun setHalftimeVisibility(visible: Boolean) {
    halftimeText.visibility = if (visible) View.VISIBLE else View.GONE
  }

  fun setStartTime(startTime: String) {
    this.startTime.text = startTime
  }

  fun setBroadcasterVisibility(visible: Boolean) {
    this.broadcaster.visibility = if (visible) View.VISIBLE else View.GONE
  }

  fun setBroadcaster(broadcaster: String) {
    this.broadcaster.text = broadcaster
  }

  fun setStreamEnabled(enabled: Boolean) {
    streamSwitch.isChecked = enabled
  }

  fun setStreamSwitchVisibility(visible: Boolean) {
    streamSwitch.visibility = if (visible) View.VISIBLE else View.GONE
  }

  fun setDelayButtonVisibility(visible: Boolean) {
    delayBtn.visibility = if (visible) View.VISIBLE else View.GONE
  }

  enum class GameState {
    PRE, LIVE, POST
  }

  sealed class GameSummaryUiEvent {
    object BackPressed : GameSummaryUiEvent()
    object StreamChecked : GameSummaryUiEvent()
    object StreamUnchecked : GameSummaryUiEvent()
    object DelayPressed : GameSummaryUiEvent()
  }
}