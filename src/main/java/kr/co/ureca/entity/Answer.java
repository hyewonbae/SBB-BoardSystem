package kr.co.ureca.entity;

import java.time.LocalDateTime;
import java.util.Set;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Entity
@Getter
@Setter
@ToString
public class Answer {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	@Column(columnDefinition = "Text")
	private String content;

	private LocalDateTime createDate;

	private LocalDateTime modifyDate;

	@ManyToOne
	@ToString.Exclude
	private Question question;

	@ManyToOne
	private SiteUser author;

	@ManyToMany
	private Set<SiteUser> voter;

}
