import 'package:dio/dio.dart';
import 'package:slidy_modular/app/shared/models/pokemon_model.dart';

class PokeRepository {
  final Dio dio;

  PokeRepository(this.dio);

  Future<List<PokemonModel>> getAllPokemons() async {
    var response = await dio.get('/pokemon');
    List<PokemonModel> list = List();

    for (var json in (response.data['results'])) {
      list.add(PokemonModel(name: json['name']));
    }

    return list;
  }
}
