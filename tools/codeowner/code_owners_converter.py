import json

def read_code_owners():
	file = open('tools/codeowner/team.json')
	list_team = json.load(file)
	with open('./CODEOWNERS') as code_owners:
		with open('./OWNERS.bits.yml', 'w') as yml:
			yml.write("code_review_rule:\r\n  files:")
			for line in code_owners:
				if line != "\n":
					module_owner = line.replace(" ", "").split("@")
					module = module_owner[0].strip()
					owner = module_owner[1].strip()
					if (owner == "tokopedia/android-minionsolo-dev"):
						owner = "tokopedia/android-minionkevin-dev"
					members = list_team.get(owner, "")
					txt1 = "\r\n    - pattern: {module}.*".format(module = module)
					txt_reviewers = ""
					if (owner is None):
						print(owner + " Member not found")
					else:
						for member in members:
							txt_reviewers += "\r\n        - {name}".format(name = member)
					reviewers = "    {txt1}\r\n      reviewers:{txt2}\r\n      required_approvals: 1".format(txt1 = txt1, txt2 = txt_reviewers)
					yml.write(reviewers)				

read_code_owners()