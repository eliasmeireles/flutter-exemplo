import 'package:mobx/mobx.dart';
import 'package:slidy_modular_02/app/shared/models/pokemon_model.dart';
import 'package:slidy_modular_02/app/shared/repositories/poke_repository.dart';

part 'home_controller.g.dart';

class HomeController = _HomeControllerBase with _$HomeController;

abstract class _HomeControllerBase with Store {
  final PokeRepository repository;

  @observable
  ObservableFuture<List<PokemonModel>> pokemons;

  _HomeControllerBase({this.repository}) {
    fetchPokemons();
  }

  @action
  void fetchPokemons() {
    pokemons = repository.getAllPokemons().asObservable();
  }

  String text;
}
