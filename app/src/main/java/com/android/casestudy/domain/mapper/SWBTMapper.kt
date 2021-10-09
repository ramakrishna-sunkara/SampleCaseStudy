package com.android.casestudy.domain.mapper

import com.android.casestudy.R
import com.android.casestudy.data.modal.*
import com.android.casestudy.data.modal.dto.ConverterResponseDTO
import com.android.casestudy.data.modal.dto.MatchResponseDTO
import com.android.casestudy.data.modal.dto.PlayerDTO
import com.android.casestudy.data.modal.dto.PlayerResponseDTO
import com.android.casestudy.util.Constants
import javax.inject.Inject

class SWBTMapper @Inject constructor() :
    Function2<List<PlayerResponseDTO>, List<MatchResponseDTO>, List<PlayersInfo?>> {
    override fun invoke(
        playersDTO: List<PlayerResponseDTO>,
        matchesDTO: List<MatchResponseDTO>
    ): List<PlayersInfo?> {

        val playerInfoList = mutableListOf<PlayersInfo>()

        val map = playersDTO.map {
            it.id to PlayerDetails(id = it.id, name = it.name, icon = it.icon)
        }.toMap()

        for (dto in playersDTO) {
            map[dto.id]?.let { player ->
                playerInfoList.add(
                    PlayersInfo(
                        name = player.name,
                        id = player.id,
                        icon = player.icon,
                        points = fetchPoints(player.id, matchesDTO),
                        matchStats = fetchMatchStats(map, player.id, matchesDTO)
                    )
                )
            }
        }
        return playerInfoList.sortedByDescending { it.points }
    }

    private fun fetchPoints(
        id: Int,
        matchesDTO: List<MatchResponseDTO>
    ): Int {

        var points = 0

        for (dto in matchesDTO) {
            if (dto.player1.id == id || dto.player2.id == id) {

                val firstPlayer: PlayerDTO
                val secondPlayer: PlayerDTO

                if (dto.player1.id == id) {
                    firstPlayer = dto.player1
                    secondPlayer = dto.player2
                } else {
                    firstPlayer = dto.player2
                    secondPlayer = dto.player1
                }

                points += when {
                    firstPlayer.score > secondPlayer.score -> 3
                    firstPlayer.score - secondPlayer.score == 0 -> 1
                    else -> continue
                }
            }

        }
        return points
    }

    private fun fetchMatchStats(
        map: Map<Int, PlayerDetails>,
        id: Int,
        matchesDTO: List<MatchResponseDTO>
    ): List<MatchStats> {
        val list = mutableListOf<MatchStats>()
        for (dto in matchesDTO) {
            dto.apply {
                val winner: Player
                val looser: Player
                if (player1.id == id || player2.id == id) {
                    val result = (player1.score - player2.score).toInt()
                    if (result >= 0) {
                        winner = Player(points = player1.score, name = map[player1.id]?.name ?: "")
                        looser = Player(points = player2.score, name = map[player2.id]?.name ?: "")
                    } else {
                        winner = Player(points = player2.score, name = map[player2.id]?.name ?: "")
                        looser = Player(points = player1.score, name = map[player1.id]?.name ?: "")
                    }
                    list.add(
                        MatchStats(
                            winner = winner, looser = looser, resultColor = when {
                                result == 0 -> R.color.white
                                result < 0 -> R.color.red
                                else -> R.color.purple_500
                            }
                        )
                    )
                }
            }
        }
        return list.toList()
    }

    fun convertQuotesResponse(result: ConverterResponseDTO): ConvertedCurrency {
        val currencyConvertedInfos: List<CurrencyConvertedInfo> = result.quotes.entries.map {
            CurrencyConvertedInfo(it.key.replace(result.source,Constants.EMPTY),it.value.toDouble()) }
        val convertedCurrency = ConvertedCurrency(result.source)
        convertedCurrency.currencyConvertedInfoList = currencyConvertedInfos
        val currencyList: ArrayList<String> = result.quotes.entries.map {
            it.key.replace(result.source, "")
        }.toList() as ArrayList<String>
        currencyList.remove("")
        currencyList.add(0, result.source)
        convertedCurrency.currencyList = currencyList
        return convertedCurrency
    }

}