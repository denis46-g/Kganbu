//Игра в кальмара : чётное-нечётное

/*
*  Правила: У игроков есть по n шариков. Играют по кругу. В каждом ходе
*  один игрок берёт в руку несколько камней, сжимает ее, а затем протягивает вперед.
*  Его сосед должен угадать, четное или нечетное количество камней лежит
*  в руке игрока. Если он угадывает, то забирает у игрока столько камней, сколько держит сам.
*  И наоборот – если не угадывает, отдает столько камней, сколько в руке у игрока.
*  Победит тот, кому по итогу достанутся все шарики.
*/

fun grammar(bet: Int, action: String) : String {
    if (bet % 10 == 1 && (bet % 100 < 10 || bet % 100 > 20))
        return "$action $bet шарик"
    else if (bet % 10 in 2..4 && (bet % 100 < 10 || bet % 100 > 20))
        return "$action $bet шарика"
    else return "$action $bet шариков"
}

fun Kganbu(balls: Int, players: MutableList<String>) {
    var total_balls = balls * players.size
    var players_to_balls = mutableMapOf<String, Int>()
    for (player in players)
        players_to_balls.put(player, balls)
    var current_players = Pair(0, 1)
    var old_size = players.size
    println("Текущее количество шариков у игроков: ")
    println(players_to_balls)
    while(players.size > 1) {
        var first_lose = false
        var max_bet = players_to_balls[players[current_players.first]]!!
        var bet = (1..max_bet).random()
        println("${players[current_players.first]} " + grammar(bet, "кладёт в руку"))
        if (max_bet > 1) {
            var hypothesis = (1..max_bet).random() % 2 == 0
            if (hypothesis)
                println("${players[current_players.second]} говорит \"чётное\"")
            else println("${players[current_players.second]} говорит \"нечётное\"")
            if (bet % 2 == 0 && hypothesis || bet % 2 == 1 && !hypothesis) {

                var old_count1 = players_to_balls[players[current_players.first]]
                var new_count1 = Math.max(0, old_count1!! - bet)
                players_to_balls[players[current_players.first]] = new_count1
                if (new_count1 > 0) {
                    println("${players[current_players.first]} " + grammar(bet, "теряет"))
                    var old_count2 = players_to_balls[players[current_players.second]]
                    var new_count2 = Math.min(total_balls, old_count2!! + bet)
                    players_to_balls[players[current_players.second]] = new_count2
                    println("${players[current_players.second]} " + grammar(bet, "получает"))
                }
                else {
                    println("${players[current_players.first]} " + grammar(old_count1, "теряет"))
                    var old_count2 = players_to_balls[players[current_players.second]]
                    var new_count2 = Math.min(total_balls, old_count2!! + old_count1)
                    players_to_balls[players[current_players.second]] = new_count2
                    println("${players[current_players.second]} " + grammar(old_count1, "получает"))
                }

                if (players_to_balls[players[current_players.first]] == 0) {
                    players_to_balls.keys.remove(players[current_players.first])
                    println("${players[current_players.first]} выбывает из игры")
                    players.removeAt(current_players.first)
                    first_lose = true
                }
            }
            else {

                var old_count1 = players_to_balls[players[current_players.second]]
                var new_count1 = Math.max(0, old_count1!! - bet)
                players_to_balls[players[current_players.second]] = new_count1
                if (new_count1 > 0) {
                    println("${players[current_players.second]} " + grammar(bet, "теряет"))
                    var old_count2 = players_to_balls[players[current_players.first]]
                    var new_count2 = Math.min(total_balls, old_count2!! + bet)
                    players_to_balls[players[current_players.first]] = new_count2
                    println("${players[current_players.first]} " + grammar(bet, "получает"))
                }
                else {
                    println("${players[current_players.second]} " + grammar(old_count1, "теряет"))
                    var old_count2 = players_to_balls[players[current_players.first]]
                    var new_count2 = Math.min(total_balls, old_count2!! + old_count1)
                    players_to_balls[players[current_players.first]] = new_count2
                    println("${players[current_players.first]} " + grammar(old_count1, "получает"))
                }

                if (players_to_balls[players[current_players.second]] == 0) {
                    players_to_balls.keys.remove(players[current_players.second])
                    println("${players[current_players.second]} выбывает из игры")
                    players.removeAt(current_players.second)
                }
            }
        }
        else {
            println("${players[current_players.second]} говорит \"нечётное\"")
            var new_count1 = players_to_balls[players[current_players.first]]!! - 1
            players_to_balls[players[current_players.first]] = new_count1
            println("${players[current_players.first]} теряет один шарик")
            var new_count2 = Math.min(total_balls, players_to_balls[players[current_players.second]]!! + 1)
            players_to_balls[players[current_players.second]] = new_count2
            println("${players[current_players.second]} " + grammar(1, "получает"))
            players_to_balls.keys.remove(players[current_players.first])
            println("${players[current_players.first]} выбывает из игры")
            players.removeAt(current_players.first)
            first_lose = true
        }
        if (old_size > players.size) {
            if (first_lose)
                current_players = Pair(current_players.first % players.size, (current_players.first + 1) % players.size)
            else {
                if (current_players.first != players.size)
                    current_players = Pair((current_players.first + 1) % players.size, (current_players.first + 2) % players.size)
                else current_players = Pair(0, 1)
            }
            old_size = players.size
        }
        else {
            current_players = Pair((current_players.first + 1) % players.size, (current_players.second + 1) % players.size)
        }
        println("Текущее количество шариков у игроков: ")
        println(players_to_balls)
    }
    println("${players[0]} побеждает")
}

fun main() {
    var balls = 5
    var players = mutableListOf("Арсений", "Дмитрий", "Сергей", "Антон")
    Kganbu(balls, players)
}