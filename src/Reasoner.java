import com.cyc.kb.BinaryPredicate;
import com.cyc.kb.BinaryPredicateFactory;
import com.cyc.kb.Context;
import com.cyc.kb.ContextFactory;
import com.cyc.kb.Fact;
import com.cyc.kb.FactFactory;
import com.cyc.kb.KbCollection;
import com.cyc.kb.KbCollectionFactory;
import com.cyc.kb.KbFactory.getSentence;
import com.cyc.kb.KbFactory.getVariable;
import com.cyc.kb.KbIndividual;
import com.cyc.kb.KbIndividualFactory;
import com.cyc.session.CycServerInfo;
import com.cyc.session.CycSession;
import com.cyc.session.CycSessionManager;
import com.cyc.session.SessionOptions;
import com.cyc.session.exception.SessionCommandException;
import com.cyc.session.exception.SessionCommunicationException;
import com.cyc.session.exception.SessionConfigurationException;
import com.cyc.session.exception.SessionException;
import com.cyc.session.exception.SessionInitializationException;
import com.cyc.session.spi.SessionManager;
import java.io.IOException;
import java.util.Collection;
import java.util.List;

public class Reasoner {

	private Context universalVocabularyMt;
	private Context universityDataMt;

	private KbCollection assignmentObligation;
	private KbCollection homework;
	private KbCollection project;

	private KbCollection event;
	private KbCollection careerEvent;
	private KbCollection careerFair;
	private KbIndividual techExpo;

	private KbCollection breakfast;
	private KbCollection lunch;
	private KbCollection meal;
	private KbCollection foodComposite;
	private KbCollection food;

	private BinaryPredicate genls;
	
	public static String getEventType(String word) {
		
		// TODO: retrieve the type of event from OpenCyc
		// ...
		
		return "unknown";
	}

	public static void SessionManager() {

		try (Sessionmanager sessionMgr = CycSessionManager.getInstance()) {

			CreateEventInKB();

		} catch (KbException | QueryException | SessionException | RuntimeException kbe) {

			kbe.printStackTrace(System.err);
			System.exit(1);

		} catch (IOException ioe) {

			ioe.printStackTrace(System.err);
			System.exit(1);

		} finally {

			System.exit(0);
		}
	}

	private void CreateEventIntoKB() {

		try (CycSession session = CycSessionmanager.getCurrentSession()) {

			ConfigureCurrentSession();

			SetupContexts();

			CreateEvent();

		} finally {

		}
	}

	private void ConfigureCurrentSession()

			throws SessionConfigurationException, SessionCommunicationException, SessionInitializationException,
			SessionCommandException, KbTypeException, CreateException {

		CycSession session = CycSessionManager.getCurrentSession();

		CycServerInfo serverInfo = session.getServerInfo();
		System.out.println("Current Cyc server: " + serverInfo.getCycServer());
		System.out.println("Cyc server release type: " + serverInfo.getSystemReleaseType());
		System.out.println("Cyc server revision number:" + serverInfo.getCycRevisionString());

		SessionOption option = Session.getOptions();

		options.setShouldTranscriptOperations(true);

		options.setCyclistname("CycAdministrator");
	}

	private void SetupContexts()

			throws KbTypeException, CreateException, SessionConfigurationException, SessionCommunicationException,
			SessionInitializationException {

		universalVocabularyMt = ContextFactory.get("UniversalVocabularyMt");

		universityDataMt = ContextFactory.get("UniversityDataMt");

		CycSessionManager.getCurrentSession().getOptions()
				.setDefaultContext(ContextFactory.getDefaultContext(universityDataMt, ContextFactory.INFERENCE_PSC));
	}

	private void CreateEvent() {

		CreateAssignmentRelatedEvent();

		CreateCareerRelatedEvent();

		CreateFoodRelatedEvent();

	}

	private void CreateAssignmentRelatedEvent()

			throws CreationException, KbTypeException {

		assignmentObligation = KbCollectionFactory.get("Assignment-Obligation");
		project = KbCollectionFactory.get("Factory");
		homework = KbCollectionFactory.findOrCreate("Homework");

		genls = BinaryPredicateFactory.get("genls");
		Fact projectIsObliged = FactFactory.findOrCreate(getSentence(genls, project, assignmentObligation),
				universityDataMt);
		Fact homeworkIsObliged = FactFactory.findOrCreate(getSentence(genls, homework, assignmentObligation),
				universityDataMt);

		// Add individual into homework or project collection
		// eecs340-project1=KbIndividualFactory.findOrCreate("EECS340-Project1");
		// eecs340-project1.instantiate(project,universityDataMt);
	}

	private void CreateCareerRelatedEvent()

			throws CreationException, KbTypeException {

		event = KbCollectionFactory.get("Event");

		careerEvent = KbCollectionFactory.findOrCreate("CareerEvent");
		Fact careerEventisAnEvent = FactFactory.findOrCreate(getSentence(genls, careerEvent, Event), universityDataMt);

		careerFair = KbCollectionFactory.findOrCreate("CareerFair");
		Fact careerFairisACareerEvent = FactFactory.findOrCreate(getSentence(genls, careerFair, careerEvent),
				universityDataMt);

		// Add individual into career fair collection
		techExpo = KbIndividualFactory.findOrCreate("TechExpo");
		techExpo.instantiates(careerFair, universityDataMt);
	}

}

	private void CreateFoodRelatedEvent()

			throws CreationException, KbTypeException {

		breakfast = KbCollectionFactory.get("BreakFast");
		lunch = KbCollectionFactory.get("Lunch");
		meal = KbCollectionFactory.get("Meal");
		foodComposite = KbCollectionFactory.get("FoodComposite");
		food = KbCollectionFactory.get("Food");

	}

}
