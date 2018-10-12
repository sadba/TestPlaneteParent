package sadba.lab.com.testplanete.Remote;


import java.util.List;

import io.reactivex.Observable;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;
import sadba.lab.com.testplanete.Model.Abscence;
import sadba.lab.com.testplanete.Model.Bulletin;
import sadba.lab.com.testplanete.Model.Commune;
import sadba.lab.com.testplanete.Model.Departement;
import sadba.lab.com.testplanete.Model.Enfant;
import sadba.lab.com.testplanete.Model.Enseignant;
import sadba.lab.com.testplanete.Model.EtabTransfert;
import sadba.lab.com.testplanete.Model.Evaluation;
import sadba.lab.com.testplanete.Model.IaResponse;
import sadba.lab.com.testplanete.Model.IefResponse;
import sadba.lab.com.testplanete.Model.InfoEtab;
import sadba.lab.com.testplanete.Model.InfosEleves;
import sadba.lab.com.testplanete.Model.Note;
import sadba.lab.com.testplanete.Model.PostRegisterUser;
import sadba.lab.com.testplanete.Model.PostUser;
import sadba.lab.com.testplanete.Model.PostVerifUser;
import sadba.lab.com.testplanete.Model.Region;
import sadba.lab.com.testplanete.Model.RegisterUser;
import sadba.lab.com.testplanete.Model.Retard;
import sadba.lab.com.testplanete.Model.Temps;
import sadba.lab.com.testplanete.Model.User;
import sadba.lab.com.testplanete.Model.VerifUser;

public interface IMyAPI {

    @Headers("Content-type: application/json")
    @POST("auth_parent")
    Call<User> loginUser(@Body PostUser postUser);

    @Headers("Content-type: application/json")
    @POST("verif_parent")
    Call<VerifUser> verifUser(@Body PostVerifUser postVerifUser);

    @Headers("Content-type: application/json")
    @POST("save_parent")
    Call<RegisterUser> registerUser(@Body PostRegisterUser postRegisterUser);

    /*@Headers("Content-type: application/json")
    @GET("enfants")
    Call<List<Enfant>> getEnfants(@Query("ien") String ien);*/

    @GET("enfants")
    Observable<List<Enfant>> getEnfants(@Query("ien") String ien);

    @GET("evaluation-eleve/index.php")
    Observable<List<Evaluation>> getEval(@Query("ien") String ien);

    @GET("infos_eleve/index.php")
    Observable<List<InfosEleves>> getInfosEleves(@Query("ien") String ien);

    @GET("etab_by_code_structure")
    Observable<InfoEtab> getInfos(@Query("code") String code);

    @GET("planning-eleve/index.php")
    Observable<List<Temps>> getTemps(@Query("ien") String ien);

    @GET("notes_eleve/index.php")
    Observable<List<Note>> getNotes(@Query("ien") String ien);

    @GET("info_bulletin/index.php")
    Observable<List<Bulletin>> getBulletins(@Query("ien") String ien);

    @GET("absence-ien/index.php")
    Observable<List<Abscence>> getAbscences(@Query("ien") String ien);

    @GET("retard-eleve/index.php")
    Observable<List<Retard>> getRetards(@Query("ien") String ien);

    @GET("liste_enseignant_classe/index.php")
    Observable<List<Enseignant>> getEnseignants(@Query("ien") String ien);

    @GET("list_regions")
    Observable<List<Region>> getRegions();

    @GET("list_departements/code/{code}")
    Observable<List<Departement>> getDepart(@Path("code") String code);

    @GET("list_communes/code/{code}")
    Observable<List<Commune>> getComm(@Path("code") String code);

    @GET("list_etab_cycle_commune/code_cyle/{code_cycle}/code_comm/{code_comm} ")
    Observable<List<EtabTransfert>> getEtab(@Path("code_cycle") String code_cycle, @Path("code_comm") String code_comm);

    @GET("api/simenv1/cycle")
    Call<IaResponse> getCycle();

    @GET("ien-get/list_structure_deconcentre")
    Call<IefResponse> getIefs(@Query("ia") String ia, @Query("type") String type);
}
