package com.nazdika.code.challenge

import android.os.Bundle
import android.util.TypedValue
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import androidx.core.text.buildSpannedString
import androidx.core.text.color
import androidx.core.view.isInvisible
import androidx.core.view.isVisible
import androidx.core.view.updateLayoutParams
import com.nazdika.code.challenge.databinding.ActivityDetailBinding

class DetailActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDetailBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val match = intent.getParcelableExtra("match") as? LiveScoreActivity.MatchModel ?: return
        with(binding) {
            layoutCompetition.apply {
                imgLogo.setImageURI(match.competition?.logo)
                tvCompetitionName.text = match.competition?.persianName ?: match.competition?.localizedName
                tvCompetitionName.typeface = ResourcesCompat.getFont(this@DetailActivity, R.font.vazir_medium)
            }
            layoutMatch.apply {
                imgHomeTeamLogo.setImageURI(match.homeTeam?.logo)
                imgAwayTemLogo.setImageURI(match.awayTeam?.logo)
                tvHomeTeamName.text = match.homeTeam?.persianName ?: match.homeTeam?.localizedName ?: match.homeTeam?.englishName
                tvHomeTeamName.typeface = ResourcesCompat.getFont(this@DetailActivity, R.font.vazir_light)
                tvAwayTeamName.text = match.awayTeam?.persianName ?: match.awayTeam?.localizedName ?: match.awayTeam?.englishName
                tvAwayTeamName.typeface = ResourcesCompat.getFont(this@DetailActivity, R.font.vazir_light)
                tvStatus.typeface = ResourcesCompat.getFont(this@DetailActivity, R.font.vazir_light)
                tvScores.typeface = ResourcesCompat.getFont(this@DetailActivity, R.font.vazir_light)
                when {
                    match.matchStarted == false && (match.matchEnded == false || match.matchEnded == true) -> {
                        tvStatus.isInvisible = true
                        tvScores.updateLayoutParams<ConstraintLayout.LayoutParams> {
                            topMargin = dpToPx(8f).toInt()
                        }
                        tvScores.text = buildSpannedString {
                            color(ContextCompat.getColor(applicationContext, R.color.gray)) {
                                append(
                                    match.status
                                )
                            }
                        }
                    }

                    else -> {
                        tvStatus.isVisible = true
                        tvScores.text = buildSpannedString {
                            if ((match.awayTeamPen ?: -1) >= 0) {
                                color(
                                    ContextCompat.getColor(
                                        applicationContext,
                                        R.color.gray
                                    )
                                ) { append("(${match.awayTeamPen})") }
                                append(" ")
                                append(" ")
                            }
                            append(
                                "${match.awayTeamScore} - ${
                                    match.homeTeamScore
                                }"
                            )
                            if ((match.homeTeamPen ?: -1) >= 0) {
                                append(" ")
                                append(" ")
                                color(
                                    ContextCompat.getColor(
                                        applicationContext,
                                        R.color.gray
                                    )
                                ) { append("(${match.homeTeamPen})") }
                            }
                        }
                        tvStatus.text = match.status
                    }
                }
            }
        }
    }

    private fun dpToPx(dp: Float): Float {
        return TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_DIP,
            dp,
            resources.displayMetrics
        )
    }
}