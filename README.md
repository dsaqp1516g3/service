# service

Okupainfo



convencions per picar el codi (codestyle)
-----------------------------------------
-----------

Noms utilitzats repetidament
-
quan es faci servir 'okupainfo' es fa tot en **minúscules** en general
```
okupainfo
```
si el cas q es fa servir és amb majúscula la 1a, només la 1a:
```
Okupainfo
```

ClassNames:
-----------
primera lletra en majúscules
```
public class OkupainfoMediaType
{
}
```

Interfaces
----------
Interface names should be capitalized like class names.
```
interface RasterDelegate;
interface Storing;
```

Mètodes
-
verb amb lowercase + la resta de paraules amb 1a lletra majúscula
```
getUsers();
User getUserById(String id) throws SQLException;
User getUserByLoginid(String loginid) throws SQLException;
User getAllUsers() throws SQLException;
User getUsersByEventoId(String eventoid) throws SQLException;
```

Variàbles
---------
firstletter amb lowercase, si són vàries paraules ajuntades, a partir de la 2a paraula es posa la inicial amb majúscula
```
var listaUsers;
var aixoEsUnExempleDeVariableLlegiblePeroLlarg;
```


Constants
---------
tot uppercase letters, i les paraules separades per underscores (barrabaixa)
```
public final static String OKUPAINFO_AUTH_TOKEN = "x";
    public final static String OKUPAINFO_USER = "x";
    public final static String OKUPAINFO_CASAL = "x";
    public final static String OKUPAINFO_CASAL_COLLECTION = "x";
    public final static String OKUPAINFO_COMMENTS_EVENTS = "x";
    public final static String OKUPAINFO_COMMENTS_EVENTS_COLLECTION = "x";
    public final static String OKUPAINFO_COMMENTS_CASALS_ = "x";
    public final static String OKUPAINFO_COMMENTS_CASALS_COLLECTION = "x";
    public final static String OKUPAINFO_EVENTS = "x";
    public final static String OKUPAINFO_EVENTS_COLLECTION = "x";
    public final static String OKUPAINFO_ROOT = "x";
```