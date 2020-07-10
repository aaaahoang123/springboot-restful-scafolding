package vn.amit.entity.auth

import javax.persistence.*

@Entity
@Table(name = "accounts")
open class Account(
        @Id
        @GeneratedValue(strategy = GenerationType.AUTO)
        open var id: Int? = null,
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        @Column(unique = true)
        open var rowGuid: String? = null,
        @Column(unique = true)
        open var username: String? = null
)