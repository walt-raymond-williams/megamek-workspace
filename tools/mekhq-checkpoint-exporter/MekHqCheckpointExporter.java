import static java.util.Arrays.sort;

import java.io.File;
import java.io.FileInputStream;
import java.io.OutputStream;
import java.io.PrintStream;
import java.lang.reflect.Method;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import megamek.client.generator.RandomCallsignGenerator;
import megamek.client.generator.RandomNameGenerator;
import megamek.common.loaders.MekSummaryCache;
import megamek.common.units.Entity;
import mekhq.MHQStaticDirectoryManager;
import mekhq.MekHQ;
import mekhq.campaign.AbstractLocation;
import mekhq.campaign.Campaign;
import mekhq.campaign.CampaignFactory;
import mekhq.campaign.finances.CurrencyManager;
import mekhq.campaign.finances.financialInstitutions.FinancialInstitutions;
import mekhq.campaign.market.unitMarket.UnitMarketOffer;
import mekhq.campaign.mission.Contract;
import mekhq.campaign.mission.Mission;
import mekhq.campaign.mission.Scenario;
import mekhq.campaign.mission.atb.AtBScenarioModifier;
import mekhq.campaign.personnel.Bloodname;
import mekhq.campaign.personnel.Person;
import mekhq.campaign.personnel.SpecialAbility;
import mekhq.campaign.personnel.backgrounds.RandomCompanyNameGenerator;
import mekhq.campaign.personnel.medical.advancedMedical.InjuryTypes;
import mekhq.campaign.personnel.ranks.Ranks;
import mekhq.campaign.personnel.skills.SkillType;
import mekhq.campaign.unit.Unit;
import mekhq.campaign.universe.Factions;
import mekhq.campaign.universe.Planet;
import mekhq.campaign.universe.PlanetarySystem;
import mekhq.campaign.universe.Systems;
import mekhq.campaign.universe.eras.Eras;

public class MekHqCheckpointExporter {
    private static final String EVIDENCE_EXPORT = "Confirmed from MekHQ export";
    private static final String EVIDENCE_SERIALIZED = "Serialized fact";
    private static final String EVIDENCE_UNSUPPORTED = "Unsupported";
    private static final String EVIDENCE_NEEDS = "Needs MekHQ inspection";

    public static void main(String[] args) throws Exception {
        if (args.length != 1) {
            throw new IllegalArgumentException("Usage: MekHqCheckpointExporter <campaign.cpnx|campaign.cpnx.gz|campaign.xml>");
        }

        System.setProperty("java.awt.headless", "true");
        PrintStream originalErr = System.err;
        System.setErr(new PrintStream(OutputStream.nullOutputStream()));
        try {
            Path input = Path.of(args[0]).toAbsolutePath();
            Campaign campaign = loadCampaign(input);
            originalErr.flush();
            System.out.println(toJson(input, campaign));
        } catch (Exception ex) {
            System.setErr(originalErr);
            throw ex;
        }
    }

    private static Campaign loadCampaign(Path input) throws Exception {
        initializeReadOnlyData();

        Method getInstance = MekHQ.class.getDeclaredMethod("getInstance");
        getInstance.setAccessible(true);
        MekHQ app = (MekHQ) getInstance.invoke(null);

        try (FileInputStream stream = new FileInputStream(input.toFile())) {
            Campaign campaign = CampaignFactory.newInstance(app).createCampaign(stream);
            if (campaign == null) {
                throw new IllegalStateException("CampaignFactory returned null");
            }
            return campaign;
        }
    }

    private static void initializeReadOnlyData() throws Exception {
        CurrencyManager.getInstance().loadCurrencies();
        Eras.initializeEras();
        FinancialInstitutions.initializeFinancialInstitutions();
        InjuryTypes.registerAll();
        Ranks.initializeRankSystems();
        SkillType.initializeTypes();
        sort(SkillType.getSkillList());
        SpecialAbility.initializeSPA(false);
        AtBScenarioModifier.initializeScenarioModifiers(false);
        Factions.setInstance(Factions.loadDefault(false));
        RandomNameGenerator.getInstance();
        RandomCallsignGenerator.getInstance();
        RandomCompanyNameGenerator.getInstance();
        Bloodname.loadBloodnameData();
        Systems.initializeDefaultSystems();
        MHQStaticDirectoryManager.initialize();
        while (!MekSummaryCache.getInstance().isInitialized()) {
            Thread.sleep(50);
        }
    }

    private static String toJson(Path input, Campaign campaign) throws Exception {
        StringBuilder out = new StringBuilder();
        String indent = "  ";
        boolean gzip = hasGzipHeader(input);
        out.append("{\n");
        out.append(indent).append("\"bridge_metadata\": {\n");
        field(out, 2, "schema_name", "mekhq-read-only-checkpoint", true);
        field(out, 2, "schema_version", "0.1", true);
        field(out, 2, "producer", "workspace-jar-backed-prototype", true);
        field(out, 2, "producer_version", "0.1", true);
        field(out, 2, "mekhq_version", String.valueOf(campaign.getVersion()), true);
        field(out, 2, "exported_at", Instant.now().toString(), true);
        field(out, 2, "input_path", input.toString(), true);
        field(out, 2, "save_timestamp", Files.getLastModifiedTime(input).toInstant().toString(), true);
        numberField(out, 2, "save_size_bytes", Files.size(input), true);
        boolField(out, 2, "gzip", gzip, true);
        boolField(out, 2, "read_only", true, true);
        field(out, 2, "checkpoint_id", "prototype-" + campaign.getLocalDate(), true);
        field(out, 2, "evidence", EVIDENCE_EXPORT, true);
        arrayOfStrings(out, 2, "warnings", List.of(
              "Prototype exporter; validate before production use.",
              "Classpath uses installed MekHQ jars and initializes MekHQ data without launching the GUI."), false);
        out.append(indent).append("},\n");

        out.append(indent).append("\"campaign\": {\n");
        valueObject(out, 2, "id", "Unknown", EVIDENCE_SERIALIZED, false, "Campaign#writeToXML(...)", true);
        valueObject(out, 2, "name", campaign.getName(), EVIDENCE_EXPORT, true, "Campaign#getName()", true);
        valueObject(out, 2, "date", campaign.getLocalDate().toString(), EVIDENCE_EXPORT, true, "Campaign#getLocalDate()", true);
        valueObject(out, 2, "start_date", "Unknown", EVIDENCE_SERIALIZED, false, "Campaign#writeToXML(...)", true);
        valueObject(out, 2, "faction", safe(() -> campaign.getFaction().getShortName(), "Unknown"), EVIDENCE_EXPORT, true, "Campaign#getFaction()", true);
        valueObject(out, 2, "gm_mode", "Unknown", EVIDENCE_SERIALIZED, false, "Campaign#writeToXML(...)", true);
        out.append("    \"location\": {\n");
        valueObject(out, 3, "current_system_id", safe(() -> campaign.getCurrentSystem().getId(), "Unknown"), EVIDENCE_EXPORT, true, "Campaign#getCurrentSystem()", true);
        valueObject(out, 3, "current_system_name", safe(() -> campaign.getCurrentSystem().getName(campaign.getLocalDate()), "Unknown"), EVIDENCE_EXPORT, true, "Campaign#getCurrentSystem()", true);
        locationObject(out, campaign, true);
        valueObject(out, 3, "travel_state", "Unknown", EVIDENCE_NEEDS, false, "Location/LocationNode", false);
        out.append("    }\n");
        out.append(indent).append("},\n");

        out.append(indent).append("\"finances\": {\n");
        moneyObject(out, 2, "balance", String.valueOf(campaign.getFinances().getBalance()), "Finances#getBalance()", true);
        moneyObject(out, 2, "loan_balance", String.valueOf(campaign.getFinances().getLoanBalance()), "Finances#getLoanBalance()", true);
        valueObject(out, 2, "has_active_loans", String.valueOf(campaign.getFinances().hasActiveLoans()), EVIDENCE_EXPORT, true, "Finances#hasActiveLoans()", true);
        out.append("    \"recent_transactions\": []\n");
        out.append(indent).append("},\n");

        personnel(out, campaign);
        units(out, campaign);
        contractsAndScenarios(out, campaign);
        logistics(out);
        markets(out, campaign);
        reports(out, campaign);
        unsupported(out);
        out.append("}\n");
        return out.toString();
    }

    private static void personnel(StringBuilder out, Campaign campaign) {
        out.append("  \"personnel\": [\n");
        List<Person> people = new ArrayList<>(campaign.getPersonnel());
        for (int i = 0; i < people.size(); i++) {
            Person p = people.get(i);
            out.append("    {\n");
            field(out, 3, "id", p.getId().toString(), true);
            field(out, 3, "display_name", p.getFullTitle(), true);
            field(out, 3, "full_title", p.getFullTitle(), true);
            rawLabel(out, 3, "primary_role", String.valueOf(p.getPrimaryRole()), p.getPrimaryRoleDesc(), "Person#getPrimaryRoleDesc()", true);
            rawLabel(out, 3, "status", String.valueOf(p.getStatus()), String.valueOf(p.getStatus()), "Person#getStatus()", true);
            field(out, 3, "unit_id", safe(() -> p.getUnit().getId().toString(), "Unknown"), true);
            valueObject(out, 3, "fatigue", String.valueOf(p.getFatigueDirect()), EVIDENCE_EXPORT, true, "Person#getFatigueDirect()", true);
            valueObject(out, 3, "hits", String.valueOf(p.getHits()), EVIDENCE_EXPORT, true, "Person#getHits()", true);
            moneyObject(out, 3, "salary", String.valueOf(p.getSalary(campaign)), "Person#getSalary(Campaign)", true);
            out.append("      \"injuries\": [],\n");
            out.append("      \"skills\": [],\n");
            out.append("      \"warnings\": []\n");
            out.append("    }").append(i + 1 < people.size() ? "," : "").append("\n");
        }
        out.append("  ],\n");
    }

    private static void units(StringBuilder out, Campaign campaign) {
        out.append("  \"units\": [\n");
        List<Unit> units = new ArrayList<>(campaign.getUnits());
        for (int i = 0; i < units.size(); i++) {
            Unit u = units.get(i);
            Entity e = u.getEntity();
            out.append("    {\n");
            field(out, 3, "id", u.getId().toString(), true);
            field(out, 3, "display_name", u.getName(), true);
            out.append("      \"entity\": {\n");
            field(out, 4, "chassis", e == null ? "Unknown" : e.getChassis(), true);
            field(out, 4, "model", e == null ? "Unknown" : e.getModel(), true);
            field(out, 4, "type", e == null ? "Unknown" : e.getClass().getSimpleName(), true);
            field(out, 4, "weight_tons", e == null ? "Unknown" : String.valueOf(e.getWeight()), true);
            field(out, 4, "source_owner", "Unit#getEntity()", false);
            out.append("      },\n");
            rawLabel(out, 3, "status", u.getStatus(), u.getStatus(), "Unit#getStatus()", true);
            field(out, 3, "scenario_id", String.valueOf(u.getScenarioId()), true);
            out.append("      \"crew\": [],\n");
            field(out, 3, "tech_id", safe(() -> u.getTech().getId().toString(), "Unknown"), true);
            field(out, 3, "engineer_id", safe(() -> u.getEngineer().getId().toString(), "Unknown"), true);
            valueObject(out, 3, "damage_state", u.getCondition(), EVIDENCE_EXPORT, true, "Unit#getCondition(); Unit#getDamageState()", true);
            out.append("      \"repair_summary\": {\n");
            numberField(out, 4, "parts_needed_count", u.getPartsNeeded().size(), true);
            numberField(out, 4, "parts_needing_service_count", u.getPartsNeedingService().size(), true);
            field(out, 4, "under_repair", String.valueOf(u.isUnderRepair()), true);
            field(out, 4, "last_maintenance_report", clean(u.getLastMaintenanceReport()), true);
            field(out, 4, "source_owner", "Unit#getPartsNeeded(); Unit#getPartsNeedingService(...); Unit#isUnderRepair(); Unit#getLastMaintenanceReport()", false);
            out.append("      },\n");
            out.append("      \"transport\": {\"ship_transport\": [], \"tactical_transport\": [], \"tow_transport\": [], \"warnings\": [\"Prototype does not validate exact transport assignment semantics.\"]},\n");
            out.append("      \"warnings\": []\n");
            out.append("    }").append(i + 1 < units.size() ? "," : "").append("\n");
        }
        out.append("  ],\n");
    }

    private static void contractsAndScenarios(StringBuilder out, Campaign campaign) {
        List<Mission> missions = new ArrayList<>(campaign.getMissions());
        out.append("  \"contracts\": [\n");
        for (int i = 0; i < missions.size(); i++) {
            Mission m = missions.get(i);
            out.append("    {\n");
            numberField(out, 3, "id", m.getId(), true);
            field(out, 3, "display_name", m.getName(), true);
            field(out, 3, "type", m.getType(), true);
            rawLabel(out, 3, "status", String.valueOf(m.getStatus()), String.valueOf(m.getStatus()), "Mission#getStatus()", true);
            field(out, 3, "system_id", m.getSystemId(), true);
            field(out, 3, "system_name", safe(() -> m.getSystemName(campaign.getLocalDate()), "Unknown"), true);
            field(out, 3, "employer", contractString(m, c -> c.getEmployer(), "Unknown"), true);
            field(out, 3, "enemy", "Unknown", true);
            field(out, 3, "start_date", contractString(m, c -> String.valueOf(c.getStartDate()), "Unknown"), true);
            field(out, 3, "end_date", contractString(m, c -> String.valueOf(c.getEndingDate()), "Unknown"), true);
            contractTerms(out, m, campaign);
            out.append("      \"scenario_ids\": [");
            for (int j = 0; j < m.getScenarios().size(); j++) {
                out.append(m.getScenarios().get(j).getId()).append(j + 1 < m.getScenarios().size() ? ", " : "");
            }
            out.append("],\n");
            out.append("      \"warnings\": []\n");
            out.append("    }").append(i + 1 < missions.size() ? "," : "").append("\n");
        }
        out.append("  ],\n");

        List<Scenario> scenarios = new ArrayList<>();
        for (Mission mission : missions) {
            scenarios.addAll(mission.getScenarios());
        }
        out.append("  \"scenarios\": [\n");
        for (int i = 0; i < scenarios.size(); i++) {
            Scenario s = scenarios.get(i);
            out.append("    {\n");
            numberField(out, 3, "id", s.getId(), true);
            numberField(out, 3, "mission_id", s.getMissionId(), true);
            field(out, 3, "display_name", s.getName(), true);
            rawLabel(out, 3, "status", String.valueOf(s.getStatus()), String.valueOf(s.getStatus()), "Scenario#getStatus()", true);
            field(out, 3, "date", String.valueOf(s.getDate()), true);
            out.append("      \"report\": {\"text\": ").append(q(clean(s.getReport()))).append(", \"sanitized\": true, \"source_owner\": \"Scenario#getReport()\"},\n");
            out.append("      \"player_force\": {\"formation_id\": \"Unknown\", \"source_owner\": \"Scenario#getForces(Campaign)\"},\n");
            out.append("      \"bot_forces\": [],\n");
            out.append("      \"warnings\": []\n");
            out.append("    }").append(i + 1 < scenarios.size() ? "," : "").append("\n");
        }
        out.append("  ],\n");
    }

    private static void logistics(StringBuilder out) {
        out.append("  \"repairs_and_logistics\": {\n");
        out.append("    \"shopping_list\": [],\n");
        out.append("    \"repair_work\": [],\n");
        out.append("    \"parts_pressure\": [],\n");
        out.append("    \"cargo\": {\"value\": \"Unknown\", \"evidence\": \"Needs MekHQ inspection\", \"method_backed\": \"Unknown\", \"source_owner\": \"CargoStatistics\", \"warnings\": [\"Prototype does not validate cargo pressure.\"]},\n");
        out.append("    \"transport_bays\": {\"value\": \"Unknown\", \"evidence\": \"Needs MekHQ inspection\", \"method_backed\": \"Unknown\", \"source_owner\": \"HangarStatistics\", \"warnings\": [\"Prototype does not validate bay occupancy.\"]},\n");
        out.append("    \"warnings\": []\n");
        out.append("  },\n");
    }

    private static void markets(StringBuilder out, Campaign campaign) {
        out.append("  \"markets\": {\n");
        out.append("    \"unit_offers\": [\n");
        List<UnitMarketOffer> offers = campaign.getUnitMarket().getOffers();
        for (int i = 0; i < offers.size(); i++) {
            UnitMarketOffer offer = offers.get(i);
            out.append("      {\n");
            out.append("        \"id\": null,\n");
            out.append("        \"stable_selector_available\": false,\n");
            field(out, 4, "display_name", offer.getUnit().getName(), true);
            field(out, 4, "market_type", String.valueOf(offer.getMarketType()), true);
            field(out, 4, "unit_type", String.valueOf(offer.getUnitType()), true);
            field(out, 4, "unit_name", offer.getUnit().getName(), true);
            numberField(out, 4, "percent", offer.getPercent(), true);
            numberField(out, 4, "transit_duration_days", offer.getTransitDuration(), true);
            moneyObject(out, 4, "final_price", String.valueOf(offer.getPrice()), "UnitMarketOffer#getPrice()", true);
            out.append("        \"entity\": {\"type\": ").append(q(safe(() -> offer.getEntity().getClass().getSimpleName(), "Unknown"))).append(", \"source_owner\": \"UnitMarketOffer#getEntity()\"},\n");
            out.append("        \"guard_fields\": [\"market_type\", \"unit_type\", \"unit_name\", \"percent\", \"transit_duration_days\"],\n");
            out.append("        \"warnings\": [\"UnitMarketOffer#writeToXML(...) does not expose a source-confirmed unique offer UUID.\"]\n");
            out.append("      }").append(i + 1 < offers.size() ? "," : "").append("\n");
        }
        out.append("    ],\n");
        out.append("    \"personnel_applicants\": [],\n");
        out.append("    \"contract_offers\": [],\n");
        out.append("    \"warnings\": []\n");
        out.append("  },\n");
    }

    private static void reports(StringBuilder out, Campaign campaign) {
        out.append("  \"reports\": {\n");
        reportArray(out, "current", campaign.getCurrentReport(), "Campaign#getCurrentReport()", true);
        reportArray(out, "technical", campaign.getTechnicalReport(), "Campaign#getTechnicalReport()", false);
        out.append("    ,\"skill\": [], \"finances\": [], \"acquisitions\": [], \"medical\": [], \"personnel\": [], \"battle\": [], \"politics\": [], \"aggregate\": [], \"warnings\": []\n");
        out.append("  },\n");
    }

    private static void unsupported(StringBuilder out) {
        out.append("  \"unsupported\": [\n");
        out.append("    {\"area\": \"markets.unit_offers\", \"field\": \"stable_offer_id\", \"reason\": \"UnitMarketOffer#writeToXML(...) does not expose a unique offer UUID.\", \"evidence\": \"Unsupported\", \"recommended_owner\": \"MekHQ exporter or future source change\", \"blocks_automation\": true},\n");
        out.append("    {\"area\": \"repairs_and_logistics.cargo\", \"field\": \"exact_cargo_pressure\", \"reason\": \"Prototype does not validate cargo pressure.\", \"evidence\": \"Needs MekHQ inspection\", \"recommended_owner\": \"MekHQ exporter validation pass\", \"blocks_automation\": false},\n");
        out.append("    {\"area\": \"contracts.enemy\", \"field\": \"enemy\", \"reason\": \"Generic Contract does not expose enemy identity; AtB-specific enemy fields need a source-owned exporter pass.\", \"evidence\": \"Needs MekHQ inspection\", \"recommended_owner\": \"MekHQ exporter hardening or source-owned exporter\", \"blocks_automation\": false},\n");
        out.append("    {\"area\": \"write_commands\", \"field\": \"all\", \"reason\": \"Prototype is read-only and implements no save writeback or campaign actions.\", \"evidence\": \"Unsupported\", \"recommended_owner\": \"Future explicit write-command issue, if approved\", \"blocks_automation\": true}\n");
        out.append("  ]\n");
    }

    private static void locationObject(StringBuilder out, Campaign campaign, boolean comma) {
        AbstractLocation location = campaign.getCurrentLocation();
        PlanetarySystem system = safeObj(() -> campaign.getCurrentSystem(), null);
        Planet planet = safeObj(() -> location == null ? null : location.getPlanet(), null);
        String display = stableLocationDisplay(campaign, location, system, planet);
        indent(out, 3).append("\"current_location\": {\n");
        field(out, 4, "display_name", display, true);
        field(out, 4, "system_id", system == null ? "Unknown" : system.getId(), true);
        field(out, 4, "system_name", system == null ? "Unknown" : system.getName(campaign.getLocalDate()), true);
        field(out, 4, "planet_id", planet == null ? "Unknown" : planet.getId(), true);
        field(out, 4, "planet_name", planet == null ? "Unknown" : planet.getName(campaign.getLocalDate()), true);
        field(out, 4, "location_type", location == null ? "Unknown" : location.getClass().getSimpleName(), true);
        boolField(out, 4, "is_on_planet", location != null && location.isOnPlanet(), true);
        boolField(out, 4, "is_at_jump_point", location != null && location.isAtJumpPoint(), true);
        boolField(out, 4, "is_in_transit", location != null && location.isInTransit(), true);
        boolField(out, 4, "is_jump_zenith", location != null && location.isJumpZenith(), true);
        field(out, 4, "transit_time_days", location == null ? "Unknown" : String.valueOf(location.getTransitTime()), true);
        field(out, 4, "percentage_transit", location == null ? "Unknown" : String.valueOf(location.getPercentageTransit()), true);
        field(out, 4, "evidence", EVIDENCE_EXPORT, true);
        boolField(out, 4, "method_backed", true, true);
        field(out, 4, "source_owner", "Campaign#getCurrentLocation(); AbstractLocation; PlanetarySystem; Planet", true);
        arrayOfStrings(out, 4, "warnings", List.of(
              "Location route/base semantics remain advisory until disposable-save UI validation."), false);
        indent(out, 3).append("}").append(comma ? "," : "").append("\n");
    }

    private static String stableLocationDisplay(Campaign campaign, AbstractLocation location, PlanetarySystem system,
          Planet planet) {
        if (location == null || system == null) {
            return "Unknown";
        }
        String systemName = system.getName(campaign.getLocalDate());
        if (location.isInTransit()) {
            return "In transit near " + systemName;
        }
        if (location.isAtJumpPoint()) {
            return (location.isJumpZenith() ? "Zenith" : "Nadir") + " jump point, " + systemName;
        }
        if (location.isOnPlanet() && planet != null) {
            return planet.getName(campaign.getLocalDate()) + ", " + systemName;
        }
        return systemName;
    }

    private static void contractTerms(StringBuilder out, Mission mission, Campaign campaign) {
        indent(out, 3).append("\"terms\": {\n");
        if (mission instanceof Contract contract) {
            field(out, 4, "source_owner", "Contract getters", true);
            numberField(out, 4, "length_months", contract.getLength(), true);
            field(out, 4, "command_rights", String.valueOf(contract.getCommandRights()), true);
            numberField(out, 4, "transport_comp_pct", contract.getTransportComp(), true);
            field(out, 4, "transport_comp", contract.getTransportCompString(), true);
            numberField(out, 4, "support_pct", contract.getStraightSupport(), true);
            field(out, 4, "support", contract.getStraightSupportString(), true);
            numberField(out, 4, "battle_loss_comp_pct", contract.getBattleLossComp(), true);
            field(out, 4, "battle_loss_comp", contract.getBattleLossCompString(), true);
            numberField(out, 4, "salvage_pct", contract.getSalvagePct(), true);
            field(out, 4, "salvage", contract.getSalvagePctString(), true);
            boolField(out, 4, "salvage_exchange", contract.isSalvageExchange(), true);
            boolField(out, 4, "can_salvage", contract.canSalvage(), true);
            numberField(out, 4, "advance_pct", contract.getAdvancePct(), true);
            numberField(out, 4, "signing_bonus_pct", contract.getSigningBonusPct(), true);
            boolField(out, 4, "mrbc_fee", contract.payMRBCFee(), true);
            field(out, 4, "base_amount", String.valueOf(contract.getBaseAmount()), true);
            field(out, 4, "support_amount", String.valueOf(contract.getSupportAmount()), true);
            field(out, 4, "transport_amount", String.valueOf(contract.getTransportAmount()), true);
            field(out, 4, "advance_amount", String.valueOf(contract.getAdvanceAmount()), true);
            field(out, 4, "total_advance_amount", String.valueOf(contract.getTotalAdvanceAmount()), true);
            field(out, 4, "monthly_payout", String.valueOf(contract.getMonthlyPayOut()), true);
            numberField(out, 4, "travel_days", safeInt(() -> contract.getTravelDays(campaign), -1), true);
            field(out, 4, "evidence", EVIDENCE_EXPORT, true);
            boolField(out, 4, "method_backed", true, true);
            arrayOfStrings(out, 4, "warnings", List.of(
                  "Contract enemy identity remains unsupported for generic Contract output.",
                  "Contract accept/decline remains out of scope for this read-only exporter."), false);
        } else {
            field(out, 4, "source_owner", "Mission; Contract getters unavailable", true);
            field(out, 4, "evidence", EVIDENCE_NEEDS, true);
            boolField(out, 4, "method_backed", false, true);
            arrayOfStrings(out, 4, "warnings", List.of(
                  "Mission is not a Contract subclass; prototype emits generic Mission fields only."), false);
        }
        indent(out, 3).append("},\n");
    }

    private static void reportArray(StringBuilder out, String name, List<String> reports, String owner, boolean comma) {
        out.append("    ").append(q(name)).append(": [");
        for (int i = 0; i < reports.size(); i++) {
            out.append("{\"category\": ").append(q(name)).append(", \"text\": ").append(q(clean(reports.get(i))))
                  .append(", \"date\": \"Unknown\", \"evidence\": \"").append(EVIDENCE_EXPORT)
                  .append("\", \"method_backed\": true, \"source_owner\": ").append(q(owner))
                  .append(", \"contains_html\": false, \"warnings\": []}");
            if (i + 1 < reports.size()) {
                out.append(", ");
            }
        }
        out.append("]").append(comma ? ",\n" : "\n");
    }

    private static void valueObject(StringBuilder out, int level, String name, String value, String evidence, boolean methodBacked, String owner, boolean comma) {
        indent(out, level).append(q(name)).append(": {\"value\": ").append(q(value)).append(", \"evidence\": ")
              .append(q(evidence)).append(", \"method_backed\": ").append(methodBacked).append(", \"source_owner\": ")
              .append(q(owner)).append(", \"warnings\": []}").append(comma ? "," : "").append("\n");
    }

    private static void moneyObject(StringBuilder out, int level, String name, String value, String owner, boolean comma) {
        indent(out, level).append(q(name)).append(": {\"value\": ").append(q(value)).append(", \"currency\": \"C-bills\", \"evidence\": ")
              .append(q(EVIDENCE_EXPORT)).append(", \"method_backed\": true, \"source_owner\": ").append(q(owner))
              .append(", \"warnings\": []}").append(comma ? "," : "").append("\n");
    }

    private static void rawLabel(StringBuilder out, int level, String name, String raw, String label, String owner, boolean comma) {
        indent(out, level).append(q(name)).append(": {\"raw_code\": ").append(q(raw)).append(", \"label\": ").append(q(label))
              .append(", \"evidence\": ").append(q(EVIDENCE_EXPORT)).append(", \"method_backed\": true, \"source_owner\": ")
              .append(q(owner)).append("}").append(comma ? "," : "").append("\n");
    }

    private static void field(StringBuilder out, int level, String name, String value, boolean comma) {
        indent(out, level).append(q(name)).append(": ").append(q(value)).append(comma ? "," : "").append("\n");
    }

    private static void numberField(StringBuilder out, int level, String name, long value, boolean comma) {
        indent(out, level).append(q(name)).append(": ").append(value).append(comma ? "," : "").append("\n");
    }

    private static void boolField(StringBuilder out, int level, String name, boolean value, boolean comma) {
        indent(out, level).append(q(name)).append(": ").append(value).append(comma ? "," : "").append("\n");
    }

    private static void arrayOfStrings(StringBuilder out, int level, String name, List<String> values, boolean comma) {
        indent(out, level).append(q(name)).append(": [");
        for (int i = 0; i < values.size(); i++) {
            out.append(q(values.get(i))).append(i + 1 < values.size() ? ", " : "");
        }
        out.append("]").append(comma ? "," : "").append("\n");
    }

    private static StringBuilder indent(StringBuilder out, int level) {
        return out.append("  ".repeat(level));
    }

    private static String q(String value) {
        if (value == null) {
            return "null";
        }
        return "\"" + value.replace("\\", "\\\\").replace("\"", "\\\"").replace("\n", "\\n").replace("\r", "") + "\"";
    }

    private static String clean(String value) {
        if (value == null || value.isBlank()) {
            return "Unknown";
        }
        return value.replaceAll("<[^>]+>", " ").replaceAll("\\s+", " ").trim();
    }

    private static boolean hasGzipHeader(Path path) throws Exception {
        byte[] header = Files.readAllBytes(path);
        return header.length >= 2 && (header[0] == (byte) 0x1f) && (header[1] == (byte) 0x8b);
    }

    private static String safe(ThrowingSupplier supplier, String fallback) {
        try {
            String value = supplier.get();
            return value == null || value.isBlank() ? fallback : value;
        } catch (Exception ex) {
            return fallback;
        }
    }

    private static <T> T safeObj(ThrowingObjectSupplier<T> supplier, T fallback) {
        try {
            T value = supplier.get();
            return value == null ? fallback : value;
        } catch (Exception ex) {
            return fallback;
        }
    }

    private static int safeInt(ThrowingIntSupplier supplier, int fallback) {
        try {
            return supplier.get();
        } catch (Exception ex) {
            return fallback;
        }
    }

    private static String contractString(Mission mission, ThrowingContractSupplier supplier, String fallback) {
        if (!(mission instanceof Contract contract)) {
            return fallback;
        }
        try {
            String value = supplier.get(contract);
            return value == null || value.isBlank() ? fallback : value;
        } catch (Exception ex) {
            return fallback;
        }
    }

    private interface ThrowingSupplier {
        String get() throws Exception;
    }

    private interface ThrowingObjectSupplier<T> {
        T get() throws Exception;
    }

    private interface ThrowingIntSupplier {
        int get() throws Exception;
    }

    private interface ThrowingContractSupplier {
        String get(Contract contract) throws Exception;
    }
}
