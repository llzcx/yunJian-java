package ccw.ruan.resume.manager.neo4j.data.repository;

import ccw.ruan.resume.manager.neo4j.data.node.*;
import org.springframework.data.neo4j.annotation.Query;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;


@Component
public interface SchoolRepository extends Neo4jRepository<UniversityLevelNode, Long> {

    @Query("MATCH (p:大学) WHERE p.name =~ $name OPTIONAL MATCH (p)-[:属于]->(q:办学层次) RETURN collect(q)")
    List<UniversityLevelNode> findSchoolLevel(@Param("name") String name);

    @Query("MATCH (p:大学) WHERE p.name =~ $name OPTIONAL MATCH (p)-[:位于]->(q:城市) RETURN collect(q)")
    List<CityNode> findCity(@Param("name") String name);

    @Query("MATCH (p:大学) WHERE p.name =~ $name OPTIONAL MATCH (p)-[:包含]->(q:学科) RETURN collect(q)")
    List<DisciplineNode> findDiscipline(@Param("name") String name);

    @Query("MATCH (p:大学) WHERE p.name =~ $name OPTIONAL MATCH (p)-[:简称]->(q:大学简称) RETURN collect(q)")
    List<UniversitySimpleNameNode> findSimple(@Param("name") String name);

    @Query("MATCH (p:大学) WHERE p.name =~ $name OPTIONAL MATCH (p)-[:主管单位]->(q:主管单位) RETURN collect(q)")
    List<SponsorNode> findSponsor(@Param("name") String name);
}